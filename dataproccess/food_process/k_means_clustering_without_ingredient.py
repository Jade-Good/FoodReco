import pandas as pd
from sklearn.feature_extraction.text import TfidfVectorizer
from sklearn.cluster import KMeans

# 파일 경로를 변경해야 할 수도 있습니다.
filename = './preprocessed_data/menu_ingredient/reordered_data.xlsx'  # 여러분의 파일 경로로 변경해 주세요

# 데이터 로드
data = pd.read_excel(filename)

# 선택된 열을 사용하여 조합된 문자열을 만듭니다.
selected_columns = ['음식명', '조리방식'] + ['음식분류2', '음식분류1']
data_selected = data[selected_columns].fillna('')
data_selected['combined'] = data_selected.apply(lambda row: ' '.join(row.values.astype(str)), axis=1)

# 리스트에 가중치를 어떻게 부여
weights = {
    '음식명': 1,  # 음식명에 높은 가중치 부여
    '조리방식': 1,  # 조리방식에 높은 가중치 부여
    # 다른 열에 대한 가중치도 설정할 수 있습니다.
    '음식분류2': 1,
    '음식분류1':1
}

# 가중치 적용
for column, weight in weights.items():
    data_selected[column] = data_selected[column].apply(lambda x: (str(x) + ' ') * weight)

# TF-IDF 벡터화
vectorizer = TfidfVectorizer()
X = vectorizer.fit_transform(data_selected['combined'])

for i in range(499, 530, 2):

    # K-means 군집화
    k = i  # 군집의 수
    model = KMeans(n_clusters=k, init='k-means++', random_state=42)
    model.fit(X)

    # 군집 레이블을 원본 데이터에 추가
    data['cluster_labels'] = model.labels_

    # 결과를 새로운 엑셀 파일로 저장
    output_filename = './preprocessed_data/menu_ingredient/weighted_clustered_new_ingredient_'+ str(k) +'_clusters_widthout_ingredient.xlsx'  # 원하는 파일명으로 변경 가능
    data.to_excel(output_filename, index=False)

