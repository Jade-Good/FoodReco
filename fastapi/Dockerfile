FROM python:latest


COPY app/main.py /app
COPY app/requirements.txt /app

RUN pip install -r requirements.txt

EXPOSE 8000

CMD uvicorn --host 0.0.0.0 --port 7777 main:app