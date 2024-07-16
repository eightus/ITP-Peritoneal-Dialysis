FROM python:3.9-slim-bookworm

# Create and configure the virtual environment outside of /app
RUN python3 -m venv /venv && \
    . /venv/bin/activate && \
    pip install --upgrade pip

WORKDIR /app

COPY requirements.txt /app/

# Install dependencies
RUN . /venv/bin/activate && \
    pip install --no-cache-dir -r /app/requirements.txt

# Copy the application code
COPY . /app

CMD ["sh", "-c", ". /venv/bin/activate && uvicorn main:app --host 0.0.0.0 --port 8000"]
