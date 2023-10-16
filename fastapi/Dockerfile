FROM python:3.11.4

WORKDIR /app

COPY ./app/main.py /app
COPY ./app/requirements.txt /app
COPY ./app/model /app/model


RUN pip install -r requirements.txt

EXPOSE 7777

CMD uvicorn --host 0.0.0.0 --port 7777 main:app