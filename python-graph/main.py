"""
Main application entry point for the FastAPI server.
"""

import uvicorn
from datetime import datetime

from fastapi import FastAPI, HTTPException
from fastapi.middleware.cors import CORSMiddleware
import matplotlib.pyplot as plt
import io
import base64

from schema import TokenRequest, Record

from pathlib import Path
import firebase_admin
from firebase_admin import credentials
from firebase_admin import firestore
from firebase_admin import auth
from google.cloud.firestore_v1.base_query import FieldFilter


service_account = Path("credentials.json")
cred = credentials.Certificate(service_account)
fbapp = firebase_admin.initialize_app(cred)

db = firestore.client()

app = FastAPI()


app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)


@app.get("/helloworld")
async def read_helloworld():
    """
    Endpoint to return a simple 'Hello, World!' message.
    """
    return {"message": "Hello, World!"}

@app.post("/add_record")
async def add_record(record: Record):
    try:
        record_data = {
                "Name": "UserTest",
                "RecordDate": record.RecordDate,
                "Blood Pressure": record.BloodPressure,
                "Heart Rate": record.HeartRate,
                "Weight": record.Weight,
                "Urine Out": record.UrineOut,
                "Time On": record.TimeOn,
                "Time Off": record.TimeOff,
                "Heater Bag Type": record.HeaterBagType,
                "Heater Bag Amount": record.HeaterBagAmount,
                "White Bag Type": record.WhiteBagType,
                "White Bag Amount": record.WhiteBagAmount,
                "Blue Bag Type": record.BlueBagType,
                "Blue Bag Type (Others)": record.BlueBagTypeOthers,
                "Blue Bag Amount": record.BlueBagAmount,
                "Type of Therapy": record.TypeOfTherapy,
                "Total Volume": record.TotalVolume,
                "Target UF": record.TargetUF,
                "Therapy Time": record.TherapyTime,
                "Fill Volume": record.FillVolume,
                "Last Fill volume": record.LastFillVolume,
                "Dextrose % Conc.": record.DextroseConc,
                "No. of Cycles": record.Cycles,
                "Initial Drain": record.InitialDrain,
                "Avg Dwell Time": record.AvgDwellTime,
                "Color of Drainage": record.ColorOfDrainage,
                "Total UF": record.TotalUF,
                "Nett UF": record.NettUF,
                "Remarks": record.Remarks
            }
        
        db.collection('Record').add(record_data)
        
        return {"message": "Record added successfully"}
    except Exception as e:
        print(e)
        raise HTTPException(status_code=401, detail="Authentication failed")


@app.post("/weight_graph")
async def graph(request: TokenRequest):
    try:
        decoded_token = auth.verify_id_token(request.token, clock_skew_seconds=10)
        name = decoded_token['name']
        
        records_ref = db.collection('Record')
        query = records_ref.where(filter=FieldFilter('Name', '==', name))
        docs = query.stream()
        records = []
        for doc in docs:
            data = doc.to_dict()
            filtered_data = {
                "Name": data.get("Name"),
                "Weight": data.get("Weight"),
                "RecordDate": data.get("RecordDate")
            }
            records.append(filtered_data)

        print(records)
        dates = [record["RecordDate"] for record in records if record["Weight"]]
        weights = [float(record["Weight"]) for record in records if record["Weight"]]
        formatted_dates = [datetime.strptime(date, '%d/%m/%Y').strftime('%d %B') for date in dates]

        # Generate the plot
        plt.figure(figsize=(6, 4)) 
        plt.plot(formatted_dates, weights, marker='o', linestyle='-', color='b')
        plt.xlabel('Date', fontsize=12)
        plt.ylabel('Weight', fontsize=12)
        plt.xticks(rotation=45, fontsize=10)
        plt.yticks(fontsize=10)
        plt.grid(True)
        plt.tight_layout()

        # Save the plot to a BytesIO object
        buf = io.BytesIO()
        plt.savefig(buf, format='png', transparent=True)
        buf.seek(0)
        plt.close()

        # Encode the image to base64
        img_base64 = base64.b64encode(buf.getvalue()).decode('utf-8')

        return {"image": img_base64}
    except Exception as e:
        print(e)
        raise HTTPException(status_code=401, detail="Authentication failed")


# Set up the exception handler
if __name__ == "__main__":
    uvicorn.run(app, host="0.0.0.0", port=8000)
