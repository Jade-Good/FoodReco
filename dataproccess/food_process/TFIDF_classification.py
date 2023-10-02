import pandas as pd
from sklearn.feature_extraction.text import TfidfVectorizer
from sklearn.metrics.pairwise import linear_kernel
import Levenshtein
import numpy as np
import math
import time
from joblib import dump, load
# 파일 경로
file_path = 'preprocessed_data/menu_ingredient/indexed_new_ingredient.xlsx'

# 파일 읽기
df = pd.read_excel(file_path)

pd.set_option('display.max_rows', None)
pd.set_option('display.max_columns', None)
start = time.time()


# 저장된 vectorizer와 tfidf_matrix 불러오기
loaded_vectorizer = load('./model/vectorizer.joblib')
loaded_tfidf_matrix = load('./model/tfidf_matrix.joblib')


# 스프링으로부터 받은 데이터라고 가정
foods = ['갈비', '오꼬노미야끼']
# 유사도와 인덱스를 저장할 리스트 초기화
similarity_indices = []
# 각 음식명에 대한 유사도 계산
for food_name in foods:

    matching_indices = df.index[df['음식명'] == food_name].tolist()
    if not matching_indices:  # 일치하는 행이 없을 경우, 다음 음식명으로 넘어갑니다.
        continue
    food_index = matching_indices[0]
    cosine_similarities = linear_kernel(loaded_tfidf_matrix[food_index], loaded_tfidf_matrix).flatten()
    # 유사도와 인덱스 저장
    top_indices = cosine_similarities.argsort()[:-50:-1]
    for index in top_indices:
        similarity_indices.append((index, cosine_similarities[index], food_name))


# # 유사도에 따라 내림차순 정렬
sorted_similarity_indices = sorted(similarity_indices, key=lambda x: x[1], reverse=True)
#

# for i in sorted_similarity_indices:
#     print(i[0])

# # 유사한 음식명과 유사도를 DataFrame으로 만들기
similar_foods_df = pd.DataFrame({
    '음식명': [df['음식명'].iloc[i[0]] for i in sorted_similarity_indices],
    '유사도': [i[1] for i in sorted_similarity_indices],
    '기준음식명': [i[2] for i in sorted_similarity_indices]
})


similar_foods_df.drop_duplicates(subset='음식명', keep='first', inplace=True)

print(time.time() - start)
# print('******************************************')
# print('재료유사도')
# print('******************************************')
#
name_similarities = []
for origin_food in foods:
    print("======================================")
    print('기준음식명: ', origin_food)
    print("======================================")
    for sim_food in similar_foods_df.values:
        if sim_food[2] == origin_food:
            print("추천받은 음식명:", sim_food[0], "/ 유사도", sim_food[1])
#
# # # 이름 유사도 계산
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
# print(time.time() - start)
#
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


# # 싫어하는 음식
#
# # 유사도와 인덱스를 저장할 리스트 초기화
# similarity_indices = []
# bad_foods = ['연포탕',
#                   '호박잎쌈',
#                   '장어초밥',
#                   '오코노미야키',
#                   '홍삼약밥',
#                   '월과채',
#                   '콩나물쫄면',
#                   '오이유부초밥',
#                   '꿍팟퐁커리',
#                   '사과양파샐러드']
# # 각 음식명에 대한 유사도 계산
# for food_name in bad_foods:
#     food_index = df.index[df['음식명'] == food_name][0]
#     cosine_similarities = linear_kernel(loaded_tfidf_matrix[food_index], loaded_tfidf_matrix).flatten()
#     # 유사도와 인덱스 저장
#     top_indices = cosine_similarities.argsort()[:-50:-1]
#     for index in top_indices:
#         similarity_indices.append((index, cosine_similarities[index], food_name))
#
#
# # # 유사도에 따라 내림차순 정렬
# sorted_similarity_indices = sorted(similarity_indices, key=lambda x: x[1], reverse=True)
# #
# # # 유사한 음식명과 유사도를 DataFrame으로 만들기
# similar_bad_foods_df = pd.DataFrame({
#     '음식명': [df['음식명'].iloc[i[0]] for i in sorted_similarity_indices],
#     '유사도': [i[1] for i in sorted_similarity_indices],
#     '기준음식명': [i[2] for i in sorted_similarity_indices]
# })
#
# similar_bad_foods_df.drop_duplicates(subset='음식명', keep='first', inplace=True)
#
#
# print(time.time() - start)
