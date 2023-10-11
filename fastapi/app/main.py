from typing import Union, List
from fastapi import FastAPI, HTTPException
from fastapi.middleware.cors import CORSMiddleware
import pandas as pd
from sklearn.metrics.pairwise import linear_kernel
import Levenshtein
from joblib import dump, load
import logging
from pydantic import BaseModel

class Food(BaseModel):
    foodSeq: int
    name: str

logging.basicConfig(level=logging.INFO)

app = FastAPI()
# CORS 미들웨어 추가
app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],  # 특정 도메인만 허용하려면 도메인 리스트를 명시합니다.
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

@app.get("/fastapi")
def read_root():
    logging.info("Root endpoint called") 
    return {"fastapi_pjt 배포 성공!"}

@app.post("/fastapi/recommend")
async def food_recommend(foods: List[Food]):
    logging.info(foods)
    pd.set_option('display.max_rows', None)

    try:

        file_path = './model/indexed_new_ingredient.xlsx'
        df = pd.read_excel(file_path)
        logging.info("엑셀 읽어옴")
        
        loaded_tfidf_matrix = load('./model/tfidf_matrix.joblib')
        name_similarities = []
        similarity_indices = []
        logging.info("모델 읽어옴")

        for food in foods:
            food_index = food.foodSeq
            cosine_similarities = linear_kernel(loaded_tfidf_matrix[food_index-1], loaded_tfidf_matrix).flatten()

            top_indices = cosine_similarities.argsort()[:-50:-1]
            for index in top_indices:
                similarity_indices.append((index, cosine_similarities[index], food.name))
        logging.info("재료유사도 계산 완료")
        sorted_similarity_indices = sorted(similarity_indices, key=lambda x: x[1], reverse=True)
        logging.info("재료유사도 정렬 완료")

        similar_foods_df = pd.DataFrame({
            'foodSeq': [i[0] for i in sorted_similarity_indices],
            'name': [df['음식명'].iloc[i[0]] for i in sorted_similarity_indices],
            'similarity': [i[1] for i in sorted_similarity_indices],
            'originName': [i[2] for i in sorted_similarity_indices]
        })
        logging.info("재료 유사도 데이터프레임 저장 완료")
        similar_foods_df.drop_duplicates(subset='name', keep='first', inplace=True)

        logging.info("재료 유사도 중복제거 완료")
        # 먹었던것도 나와야함
        for sim_food in similar_foods_df.itertuples():
            if sim_food.name != sim_food.originName and sim_food.similarity >= 0.6:
                name_similarity = Levenshtein.ratio(sim_food.name, sim_food.originName)
                logging.info(name_similarity)
                if 0 < name_similarity < 0.5:
                    name_similarities.append({
                        'foodSeq': sim_food.foodSeq+1,
                        'similarity': sim_food.similarity,
                        'name': sim_food.name,
                        'nameSimilarity': name_similarity,
                        'originName': sim_food.originName
                    })
        logging.info("이름 유사도 비교 완료")
        logging.info(name_similarities)
        return name_similarities

    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))
    
    
@app.get("/items/{item_id}")
def read_item(item_id: int, q: Union[str, None] = None):
    return {"item_id": item_id, "q": q}
