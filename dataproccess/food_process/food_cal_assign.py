import pandas as pd
from sklearn.feature_extraction.text import TfidfVectorizer
from sklearn.metrics.pairwise import cosine_similarity
import numpy as np
import copy

file_path = './preprocessed_data/menu_ingredient/delete_duplicated.xlsx'
file_path2 = './preprocessed_data/food_calorie/delete_duplicated_food_cal.xlsx'

data = pd.read_excel(file_path)
data2 = pd.read_excel(file_path2)

df_cleaned = data.dropna(how='all')
df_cleaned2 = data2.dropna(how='all')

data = df_cleaned
data2 = df_cleaned2

# '음식명' 열에 대한 데이터 준비
food_names1 = data['음식명'].dropna().unique().tolist()
food_names2 = data2['식품명'].dropna().unique().tolist()

# TF-IDF 계산
vectorizer = TfidfVectorizer(analyzer='char', ngram_range=(1, 2))
tfidf_matrix1 = vectorizer.fit_transform(food_names1)
tfidf_matrix2 = vectorizer.transform(food_names2)

# Extract columns before '음식분류2'
pre_columns = list(data.columns[:data.columns.get_loc("알류") + 1])
# Reorder the columns of the DataFrame
food_cal_data = copy.deepcopy(data[pre_columns])

food_cal_data['칼로리']=None

# 유사도가 0.35 이상인 경우 칼로리 값 추가
for idx1, name1 in enumerate(food_names1):
    for idx2, name2 in enumerate(food_names2):
        cosine_sim = cosine_similarity(tfidf_matrix1[idx1:idx1 + 1], tfidf_matrix2[idx2:idx2 + 1])
        if cosine_sim >= 0.35:
            indices = data.index[data['음식명'] == name1].tolist()
            food_cal_data.loc[indices, '칼로리'] = data2.loc[idx2, '에너지(㎉)']

print("끝")
# DataFrame을 엑셀 파일로 저장
output_file_path = './preprocessed_data/food_calorie/combined_food_cal.xlsx'
food_cal_data.to_excel(output_file_path, index=False)