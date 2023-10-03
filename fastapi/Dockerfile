FROM python:latest

WORKDIR /app

COPY ./main.py /app
COPY ./requirements.txt /app

RUN pip install -r requirements.txt

EXPOSE 7777

CMD uvicorn --host 0.0.0.0 --port 7777 main:app