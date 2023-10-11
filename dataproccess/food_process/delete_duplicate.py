import pandas as pd
from sklearn.feature_extraction.text import TfidfVectorizer
from sklearn.metrics.pairwise import cosine_similarity
import numpy as np

# 파일에서 데이터 불러오기
# file_path = './preprocessed_data/menu_ingredient/reordered_deleted_data.xlsx'
file_path = './preprocessed_data/food_calorie/food_cal.xlsx'
data = pd.read_excel(file_path)

df_cleaned = data.dropna(how='all')
data = df_cleaned
# '음식명' 열에 대한 데이터 준비
# food_names = data['음식명'].dropna().unique().tolist()
food_names = data['식품명'].dropna().unique().tolist()

# TF-IDF 계산
vectorizer = TfidfVectorizer(analyzer='char', ngram_range=(1, 2))
tfidf_matrix = vectorizer.fit_transform(food_names)

# 코사인 유사도 계산
cosine_sim = cosine_similarity(tfidf_matrix, tfidf_matrix)

# 유사도가 0.8 이상이면서 길이가 짧은 데이터만 남기기
to_keep = set()
for i in range(len(food_names)):
    similar_indices = np.where(cosine_sim[i] >= 0.7)[0]
    if len(similar_indices) > 1:
        shortest_idx = min(similar_indices, key=lambda idx: len(food_names[idx]))
        to_keep.add(food_names[shortest_idx])
    else:
        to_keep.add(food_names[i])

# '음식명' 열에서 중복된 행을 삭제하되, 첫 번째 행만 남기기
# data_dropped = data.drop_duplicates(subset='음식명', keep='first')
data_dropped = data.drop_duplicates(subset='식품명', keep='first')
data = data_dropped

df_cleaned = data.dropna(how='all')
data = df_cleaned


filtered_data = data[data['식품명'].isin(to_keep)]
# 변경된 데이터 저장
output_path = './preprocessed_data/menu_ingredient/delete_duplicated.xlsx'
# 중복 제거된 칼로리 데이터
# output_path = './preprocessed_data/food_calorie/delete_duplicated_food_cal.xlsx'

filtered_data.to_excel(output_path, index=False)