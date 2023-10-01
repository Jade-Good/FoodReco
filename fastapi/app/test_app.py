from fastapi.testclient import TestClient
from .main import app
from .food import Food
from typing import List

def test_read_root():
    client = TestClient(app)
    foods = ['피자', '감자탕', '참치샌드위치', '매운떡볶이', "설렁탕"]

    foods = [
        Food(id=0, name="갈비"),
        Food(id=7, name="오코노미야키"),
        Food(id=14, name="김치볶음밥"),

    ]
    foods_dict = [food for food in foods]

    response = client.post(
        "/fastapi/recommend",  # 여기에 실제 엔드포인트 경로를 적어주세요.
        json=foods_dict  # 요청 본문에 foods 리스트를 json 형태로 포함합니다.
    )

    assert response.status_code == 200
    print(response)