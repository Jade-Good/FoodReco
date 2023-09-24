FROM python:latest

WORKDIR /app

COPY ./src/main.py /app
COPY ./src/requirements.txt /app

RUN pip install -r requirements.txt

EXPOSE 8000

CMD uvicorn --host 0.0.0.0 --port 8000 main:app