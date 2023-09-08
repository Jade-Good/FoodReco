import numpy as np
from sklearn.decomposition import randomized_svd, non_negative_factorization
from surprise import Dataset
def compute_cos_sim(v1, v2):
    norm1 = np.sqrt(np.sum(np.square(v1)))
    norm2 = np.sqrt(np.sum(np.square(v2)))
    dot = np.dot(v1, v2)
    return dot / (norm1 * norm2)

data = Dataset.load_builtin('ml-100k', prompt=False)
raw_data = np.array(data.raw_ratings, dtype=int)

print(raw_data)
# 모든 행에서 0번째 데이터
print(raw_data[:,0])
#모든 행에서 1번째 데이터
print(raw_data[:,1])

raw_data[:,0] -= 1
raw_data[:,1] -= 1

# 유저 수 뽑기
n_users = np.max(raw_data[:, 0])
#영화 수 뽑기
n_movies = np.max(raw_data[:, 1])
shape = (n_users+1, n_movies+1)

adj_matrix = np.ndarray(shape, dtype=int)
for user_id, movie_id, rating, time in raw_data:
    adj_matrix[user_id][movie_id] = rating

# U, V가 사용자, 아이템/ S가 특이값 벡터
U, S, V = randomized_svd(adj_matrix, n_components=2)
S = np.diag(S)

print(U.shape)
print(S.shape)
print(V.shape)

# 행렬 복원
np.matmul(np.matmul(U, S),V)

# 나는 유저번호 0번 가정
my_id, my_vector = 0, U[0]
# 나와 가장 유사한 유저 찾기
#코사인 유사도로 계산
best_match, best_match_id, best_match_vector = -1, -1, []
# 나와 가장 유사한 유저 찾기
for user_id, user_vector in enumerate(U):
    if my_id != user_id:
        cos_sim = compute_cos_sim(my_vector, user_vector)
        if cos_sim > best_match:
            best_match = cos_sim
            best_match_id = user_id
            best_match_vector = user_vector

print(best_match, best_match_id, best_match_vector)


recommend_list = []
# 사용자 기반 추천
# 나와 비슷한 취향을 가진 다른사람의 행동을 추천
# 사용자 특징 벡터의 유사도 사용(SVD)
for i, log in enumerate(zip(adj_matrix[my_id], adj_matrix[best_match_id])):
    log1, log2 = log
    if log1 < 1 and log2 > 0:
        recommend_list.append(i)
print("사용자 기반 추천 리스트",recommend_list)

# 항목 기반 추천
# 내가 본 항목과 비슷한 항목을 추천
# 항목 특징 벡터의 유사도 사용
# 나는 유저번호 0번 가정
my_id, my_vector = 0, V.T[0]
# 나와 가장 유사한 유저 찾기
#코사인 유사도로 계산
best_match, best_match_id, best_match_vector = -1, -1, []
# 나와 가장 유사한 유저 찾기
for user_id, user_vector in enumerate(V.T):
    if my_id != user_id:
        cos_sim = compute_cos_sim(my_vector, user_vector)
        if cos_sim > best_match:
            best_match = cos_sim
            best_match_id = user_id
            best_match_vector = user_vector

print(best_match, best_match_id, best_match_vector)
recommend_list = []
for i, user_vector in enumerate(adj_matrix):
    if adj_matrix[i][my_id] > 0.9:
        recommend_list.append(i)
print("항목 기반 추천 리스트",recommend_list)


# 비음수 행렬 분해를 사용한 하이브리드 추천
A, B, iter = non_negative_factorization(adj_matrix, n_components=2)
np.matmul(A, B)

# 나는 유저번호 0번 가정
my_id, my_vector = 0, U[0]
# 나와 가장 유사한 유저 찾기
#코사인 유사도로 계산
best_match, best_match_id, best_match_vector = -1, -1, []
# 나와 가장 유사한 유저 찾기
for user_id, user_vector in enumerate(U):
    if my_id != user_id:
        cos_sim = compute_cos_sim(my_vector, user_vector)
        if cos_sim > best_match:
            best_match = cos_sim
            best_match_id = user_id
            best_match_vector = user_vector

print(best_match, best_match_id, best_match_vector)
recommend_list = []
for i, log in enumerate(zip(adj_matrix[my_id], adj_matrix[best_match_id])):
    log1, log2 = log
    if log1 < 1 and log2 > 0:
        recommend_list.append(i)
print("사용자 기반 추천 리스트",recommend_list)

# 나는 유저번호 0번 가정
my_id, my_vector = 0, V.T[0]
# 나와 가장 유사한 유저 찾기
#코사인 유사도로 계산
best_match, best_match_id, best_match_vector = -1, -1, []
# 나와 가장 유사한 유저 찾기
for user_id, user_vector in enumerate(V.T):
    if my_id != user_id:
        cos_sim = compute_cos_sim(my_vector, user_vector)
        if cos_sim > best_match:
            best_match = cos_sim
            best_match_id = user_id
            best_match_vector = user_vector

print(best_match, best_match_id, best_match_vector)
recommend_list = []
for i, user_vector in enumerate(adj_matrix):
    if adj_matrix[i][my_id] > 0.9:
        recommend_list.append(i)
print("항목 기반 추천 리스트",recommend_list)
