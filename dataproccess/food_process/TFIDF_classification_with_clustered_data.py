import pandas as pd
from sklearn.feature_extraction.text import TfidfVectorizer
from sklearn.metrics.pairwise import linear_kernel
import Levenshtein
import numpy as np
import math
import time

start = time.time()
# 파일 경로
file_path = './preprocessed_data/menu_ingredient/weighted_clustered_new_ingredient_799_clusters.xlsx'

# 파일 읽기
df = pd.read_excel(file_path)

# 데이터의 처음 몇 행 확인
df.head()

# 음식명을 제외한 열을 선택하여 각 행을 하나의 문자열로 만듭니다.
df.fillna('', inplace=True)
columns = df.columns[2:]
df['combined'] = df[columns].apply(lambda row: ' '.join(row.values.astype(str)), axis=1)

# TF-IDF 변환을 수행합니다.
vectorizer = TfidfVectorizer()
tfidf_matrix = vectorizer.fit_transform(df['combined'])

## 유사도와 인덱스를 저장할 리스트 초기화
similarity_indices = []
foods = ['돼지고추장불고기',
                  '크래미계란토스트',
                  '삼각주먹밥구이',
                  '해물양파덮밥',
                  '자몽드레싱',
                  '오징어순대전',
                  '만두동',
                  '고등어무조림',
                  '찜닭',
                  '시금치볶음밥']
# 각 음식명에 대한 유사도 계산
for food_name in foods:

    food_index = df.index[df['음식명'] == food_name][0]
    print(food_index)
    cosine_similarities = linear_kernel(tfidf_matrix[food_index], tfidf_matrix).flatten()
    # 유사도와 인덱스 저장
    top_indices = cosine_similarities.argsort()[:-50:-1]
    for index in top_indices:
        similarity_indices.append((index, cosine_similarities[index], food_name))


# 유사도에 따라 내림차순 정렬
sorted_similarity_indices = sorted(similarity_indices, key=lambda x: x[1], reverse=True)
#
# # 유사한 음식명과 유사도를 DataFrame으로 만들기
similar_foods_df = pd.DataFrame({
    '음식명': [df['음식명'].iloc[i[0]] for i in sorted_similarity_indices],
    '유사도': [i[1] for i in sorted_similarity_indices],
    '기준음식명': [i[2] for i in sorted_similarity_indices]
})


similar_foods_df.drop_duplicates(subset='음식명', keep='first', inplace=True)

print(time.time() - start)
print('******************************************')
print('재료유사도')
print('******************************************')

name_similarities = []
for origin_food in foods:
    print("======================================")
    print('기준음식명: ', origin_food)
    print("======================================")
    for sim_food in similar_foods_df.values:
        if sim_food[2] == origin_food:
            print("추천받은 음식명:", sim_food[0], "/ 유사도", sim_food[1])

# 이름 유사도 계산
for sim_food in similar_foods_df.values:
    if sim_food[0] != sim_food[2] and sim_food[1] >= 0.8:
        similarity = Levenshtein.ratio(sim_food[0], sim_food[2])
        # 이름 유사도가 0.3미만인 것들
        if similarity < 0.3:
            sim_food[1] = similarity
            name_similarities.append({
                '음식명': sim_food[0],
                '유사도': sim_food[1],
                '기준음식명': sim_food[2]
                                      })
print(time.time() - start)

print('******************************************')
print('재료 + 이름 유사도')
print('******************************************')


sorted_name_similarities = sorted(name_similarities, key=lambda x: x['유사도'], reverse=True)
name_similarities = []
for origin_food in foods:
    print("======================================")
    print('기준음식명: ', origin_food)
    print("======================================")
    for sim_food in sorted_name_similarities:
        if sim_food['기준음식명'] == origin_food:
            print("추천받은 음식명:", sim_food['음식명'], "/ 유사도", sim_food["유사도"])


# 싫어하는 음식

# 유사도와 인덱스를 저장할 리스트 초기화
similarity_indices = []
bad_foods = ['연포탕',
                  '호박잎쌈',
                  '장어초밥',
                  '오코노미야키',
                  '홍삼약밥',
                  '월과채',
                  '콩나물쫄면',
                  '오이유부초밥',
                  '꿍팟퐁커리',
                  '사과양파샐러드']
# 각 음식명에 대한 유사도 계산
for food_name in bad_foods:
    food_index = df.index[df['음식명'] == food_name][0]
    cosine_similarities = linear_kernel(tfidf_matrix[food_index], tfidf_matrix).flatten()
    # 유사도와 인덱스 저장
    top_indices = cosine_similarities.argsort()[:-50:-1]
    for index in top_indices:
        similarity_indices.append((index, cosine_similarities[index], food_name))


# # 유사도에 따라 내림차순 정렬
sorted_similarity_indices = sorted(similarity_indices, key=lambda x: x[1], reverse=True)
#
# # 유사한 음식명과 유사도를 DataFrame으로 만들기
similar_bad_foods_df = pd.DataFrame({
    '음식명': [df['음식명'].iloc[i[0]] for i in sorted_similarity_indices],
    '유사도': [i[1] for i in sorted_similarity_indices],
    '기준음식명': [i[2] for i in sorted_similarity_indices]
})

similar_bad_foods_df.drop_duplicates(subset='음식명', keep='first', inplace=True)


print(time.time() - start)
