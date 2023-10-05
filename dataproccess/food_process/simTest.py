from sklearn.feature_extraction.text import TfidfVectorizer
from sklearn.metrics.pairwise import linear_kernel

# 데이터 준비
texts = ["굽기 건어물류 메인반찬  양념   간장",
         "볶음 가공식품류 면/만두 채소                       밀가루 토마토"]

# TF-IDF 변환
vectorizer = TfidfVectorizer()
tfidf_matrix = vectorizer.fit_transform(texts)

# 유사도 계산
cosine_sim = linear_kernel(tfidf_matrix, tfidf_matrix)

# 유사도 출력
print("첫 번째 행과 두 번째 행 간의 유사도:", cosine_sim[0][1])