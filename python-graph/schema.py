from pydantic import BaseModel

class TokenRequest(BaseModel):
    token: str

class Record(BaseModel):
    RecordDate: str
    BloodPressure: str
    HeartRate: int
    Weight: float
    UrineOut: float
    TimeOn: str
    TimeOff: str
    HeaterBagType: str
    HeaterBagAmount: float
    WhiteBagType: str
    WhiteBagAmount: float
    BlueBagType: str
    BlueBagTypeOthers: str
    BlueBagAmount: float
    TypeOfTherapy: str
    TotalVolume: float
    TargetUF: str
    TherapyTime: float
    FillVolume: float
    LastFillVolume: float
    DextroseConc: str
    Cycles: int
    InitialDrain: float
    AvgDwellTime: float
    ColorOfDrainage: str
    TotalUF: float
    NettUF: float
    Remarks: str