import numpy as np
from surprise import Dataset

def compute_cos_sim(v1, v2):
    norm1 = np.sqrt(np.sum(np.square(v1)))
    norm2 = np.sqrt(np.sum(np.square(v2)))
    dot = np.dot(v1, v2)
    return dot / (norm1 * norm2)

data = Dataset.load_builtin('ml-100k', prompt=False)

raw_data = np.array(data.raw_ratings, dtype=int)
# user_id, movie_id, rating, time
print(raw_data)
raw_data[:,0] -= 1
raw_data[:,1] -= 1

n_users = np.max(raw_data[:,0])
n_movies = np.max(raw_data[:,1])
shape = (n_users + 1, n_movies + 1)
# print(shape)


# 기존 방법에 명시적 피드백(사용자가 평가한 영화 점수)을 추가해 실험
adj_matrix = np.ndarray(shape, dtype=int)
for user_id, movie_id, rating, time in raw_data:
    adj_matrix[user_id][movie_id] = rating
print(adj_matrix)

# 나는 유저번호 0번 가정
my_id, my_vector = 0, adj_matrix[0]
best_match, best_match_id, best_match_vector = -1, -1, []
# 나와 가장 유사한 유저 찾기
# 벡터 내적을 통해 유사도 계산
for user_id, user_vector in enumerate(adj_matrix):
    if my_id != user_id:
        similarity = np.dot(my_vector, user_vector)
        if similarity > best_match:
            best_match = similarity
            best_match_id = user_id
            best_match_vector = user_vector
print("내적: ", best_match, best_match_id, best_match_vector)
recommend_list = []
# 나와 가장 유사한 유저를 비교하여 나를 위한 추천 영화를 추천 영화 리스트에 추가
for i, log in enumerate(zip(my_vector, best_match_vector)):
    log1, log2 = log
    if log1 < 1 and log2 > 0:
        recommend_list.append(i)
print(recommend_list)
# 나는 유저번호 0번 가정
best_match, best_match_id, best_match_vector = 9999, -1, []
# 나와 가장 유사한 유저 찾기
# 유클리드 거리
for user_id, user_vector in enumerate(adj_matrix):
    if my_id != user_id:
        euclidian_dist = np.sqrt(np.sum(np.square(my_vector - user_vector)))
        if euclidian_dist < best_match:
            best_match = euclidian_dist
            best_match_id = user_id
            best_match_vector = user_vector
print("유클리드 거리: ", best_match, best_match_id, best_match_vector)
recommend_list = []
# 나와 가장 유사한 유저를 비교하여 나를 위한 추천 영화를 추천 영화 리스트에 추가
for i, log in enumerate(zip(my_vector, best_match_vector)):
    log1, log2 = log
    if log1 < 1 and log2 > 0:
        recommend_list.append(i)
print(recommend_list)

best_match, best_match_id, best_match_vector = -1, -1, []
# 나와 가장 유사한 유저 찾기
# 코사인 유사도
for user_id, user_vector in enumerate(adj_matrix):
    if my_id != user_id:
        cos_sim = compute_cos_sim(my_vector, user_vector)
        if cos_sim > best_match:
            best_match = cos_sim
            best_match_id = user_id
            best_match_vector = user_vector
print("코사인 유사도: ", best_match, best_match_id, best_match_vector)

recommend_list = []
# 나와 가장 유사한 유저를 비교하여 나를 위한 추천 영화를 추천 영화 리스트에 추가
for i, log in enumerate(zip(my_vector, best_match_vector)):
    log1, log2 = log
    if log1 < 1 and log2 > 0:
        recommend_list.append(i)
print(recommend_list)


#내적과 코사인 유사도는 유사한듯?