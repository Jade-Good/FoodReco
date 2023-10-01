import pandas as pd

# 데이터 불러오기
data_path = './preprocessed_data/menu_ingredient/delete_duplicated.xlsx'  # 여기에 실제 파일 경로를 입력해주세요.
data = pd.read_excel(data_path)


allergy_ingredient_category_dict = {
    '국수': '밀가루',
    '스팸': '돼지고기',
    '소시지': '돼지고기',
    '베이컨':'돼지고기',
    '샌드위치':'밀가루',
    '햄버거':'밀가루',
    '또띠아':'밀가루',
    '스파게티':'밀가루',
    '토스트':'밀가루',
    '쉬림프':'갑각류',
    '파스타':'밀가루'
}


# '음식명' 열에서 문자열 대체 목록을 사용하여 문자열 일치 검사 및 F열 이후에 중복되지 않게 추가
for i, row in data.iterrows():
    # print(row.values[4:])
    for original, replacement in allergy_ingredient_category_dict.items():
        if original in row['음식명'] and replacement not in row.values[5:]:
            # 해당 행에서 비어있는 첫 번째 열 찾기
            empty_cols = row.iloc[5:].index[row.iloc[5:].isna()]
            if not empty_cols.empty:
                first_empty_col = empty_cols[0]
                data.loc[i, first_empty_col] = replacement



column_names = data.columns.tolist()


for i, row in data.iterrows():
    for original, replacement in allergy_ingredient_category_dict.items():
        if original in row.values[5:] and replacement not in row.values[5:]:
            # if row['음식명'] == '해물볶음라면':
            #     print(original, replacement)
            # 해당 행에서 비어있는 첫 번째 열 찾기
            empty_cols = row.iloc[5:].index[row.iloc[5:].isna()]
            if not empty_cols.empty:
                first_empty_col = empty_cols[0]
                data.loc[i, first_empty_col] = replacement


# 변경된 데이터 저장
output_path = './preprocessed_data/menu_ingredient/new_ingredient_deleted.xlsx'  # 여기에 저장할 파일 경로를 입력해주세요.
data.to_excel(output_path, index=False)