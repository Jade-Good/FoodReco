import numpy as np
from surprise import Dataset
data = Dataset.load_builtin('ml-100k', prompt=False)

raw_data = np.array(data.raw_ratings, dtype=int)
# user_id, movie_id, rating, time
# print(raw_data)
raw_data[:,0] -= 1
raw_data[:,1] -= 1

n_users = np.max(raw_data[:,0])
n_movies = np.max(raw_data[:,1])
shape = (n_users + 1, n_movies + 1)
# print(shape)

adj_matrix = np.ndarray(shape, dtype=int)
# 인접행렬 생성
for user_id, movie_id, rating, time in raw_data:
    adj_matrix[user_id][movie_id] = 1
# print(adj_matrix)

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
print(best_match, best_match_id, best_match_vector)

recommend_list = []
# 나와 가장 유사한 유저를 비교하여 나를 위한 추천 영화를 추천 영화 리스트에 추가
for i, log in enumerate(zip(my_vector, best_match_vector)):
    log1, log2 = log
    if log1 < 1 and log2 > 0:
        recommend_list.append(i)
print(recommend_list)




