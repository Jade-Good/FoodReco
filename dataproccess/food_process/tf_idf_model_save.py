from joblib import dump, load
import pandas as pd
from sklearn.feature_extraction.text import TfidfVectorizer
from sklearn.metrics.pairwise import linear_kernel
import numpy as np
import time
file_path = 'preprocessed_data/menu_ingredient/indexed_new_ingredient.xlsx'

# 파일 읽기
df = pd.read_excel(file_path)


# 음식명을 제외한 열을 선택하여 각 행을 하나의 문자열로 만듭니다.
df.fillna('', inplace=True)
columns = df.columns[2:]
df['combined'] = df[columns].apply(lambda row: ' '.join(row.values.astype(str)), axis=1)
print(df['combined'])

# TF-IDF 변환을 수행합니다.
vectorizer = TfidfVectorizer()
tfidf_matrix = vectorizer.fit_transform(df['combined'])

# vectorizer와 tfidf_matrix를 파일로 저장
dump(vectorizer, './model/vectorizer.joblib')
dump(tfidf_matrix, './model/tfidf_matrix.joblib')
