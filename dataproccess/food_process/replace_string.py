import pandas as pd

# 데이터 불러오기
data_path = './preprocessed_data/menu_ingredient/fine_data.xlsx'  # 여기에 실제 파일 경로를 입력해주세요.
data = pd.read_excel(data_path)


replacement_dict2 = {
    '콘': '채소',
    '옥수수': '채소',
    '초장': '양념',
    '들기름': '양념',
    '참기름':'양념',
    '쌈장': '양념',
    '마늘': '채소',
    '커리': '카레',
    '카레': '카레',
    '된장': '된장',
    '간장': '간장',
    '고추장': '고추장',
    '케챱': '케찹',
    '케첩': '케찹',
    '케찹': '케찹',
    '케쳡': '케찹',
    '오리': '오리고기',
    '한우': '소고기',
    '아롱사태': '소고기',
    '어묵': '어묵',
    '오뎅': '어묵',
    '오이': '오이',
    '당근': '채소',
    '양파': '채소',
    '피망': '채소',
    '버섯': '버섯',
    '고추': '고추',
    '닭': '닭고기',
    '소고기': '소고기',
    '돼지고기': '돼지고기',
    '생선': '생선',
    '조개': '조개',
    '고등어': '생선',
    '북어': '생선',
    '오징어': '오징어류',
    '콩': '대두',
    '두부': '대두',
    '브로콜리': '채소',
    '배추': '채소',
    '상추': '채소',
    '파': '채소',
    '강황': '카레',
    '진간장': '간장',
    '생강': '채소',
    '전복': '조개류',
    '굴': '조개류',
    '바지락': '조개류',
    '대구': '생선',
    '명태': '생선',
    '갈치': '생선',
    '광어': '생선',
    '우럭': '생선',
    '참치': '생선',
    '도미': '생선',
    '돔': '생선',
    '연어': '생선',
    '장어': '생선',
    '민어': '생선',
    '삼치': '생선',
    '조기': '생선',
    '방어': '생선',
    '숭어': '생선',
    '날치': '생선',
    '아귀': '생선',
    '코다리': '생선',
    '굴비': '생선',
    '병어': '생선',
    '전어': '생선',
    '소라': '조개류',
    '낙지': '오징어류',
    '동태': '생선',
    '추어': '생선',
    '홍어': '생선',
    '아구': '생선',
    '문어': '오징어류',
    '쭈꾸미':'오징어류',
    '도다리': '생선',
    '농어': '생선',
    '서대': '생선',
    '송어': '생선',
    '볼락': '생선',
    '청어': '생선',
    '노가리': '생선',
    '돼지': '돼지고기',
    '소갈비': '소고기',
    '쇠고기': '소고기',
    '제육': '돼지고기',
    '양갈비': '양고기',
    '소불고기': '소고기',
    '가오리': '생선',
    '메기': '생선',
    '임연수': '생선',
    '과메기': '생선',
    '안심': '육류',
    '목살': '육류',
    '삼겹살': '돼지고기',
    '항정살': '돼지고기',
    '오리고기': '오리고기',
    '양지머리': '소고기',
    '가지': '채소',
    '고구마': '채소',
    '감자': '채소',
    '콜리플라워': '채소',
    '파프리카': '채소',
    '황태':'생선',
    '홍합':'조개류',
    '치킨': '닭고기',
    '가자미': '생선'

}


for i, row in data.iterrows():
    for original, replacement in replacement_dict2.items():
        if original in row['음식명'] and replacement not in row.values[5:]:
            # 해당 행에서 비어있는 첫 번째 열 찾기
            empty_cols = row.iloc[5:].index[row.iloc[5:].isna()]
            if not empty_cols.empty:
                first_empty_col = empty_cols[0]
                data.loc[i, first_empty_col] = replacement

category_dict = {
    '닭고기': '닭고기',
    '돼지고기': '돼지고기',
    '밀가루': '밀가루',
    '육류': '육류',
    '소고기': '소고기'
}


for i, row in data.iterrows():
    for category, add in category_dict.items():
        if category in row['음식분류1'] and add not in row.values[5:]:
            # if category == '돼지고기':
            #     print(row['음식명'], row.values[5:])
            empty_cols = row.iloc[5:].index[row.iloc[5:].isna()]
            if not empty_cols.empty:
                first_empty_col = empty_cols[0]
                data.loc[i, first_empty_col] = add
allergy_ingredient_category_dict = {
    '카슈넛':'견과류',
    '유부':'대두',
    '두부':'대두',
    '콩':'대두',
    '수제비':'밀가루',
    '땅콩버터': '땅콩',
    '라면': '밀가루',
    '토마토': '토마토',
    '복숭아': '복숭아',
    '새우': '갑각류',
    '꽃게': '갑각류',
    '랍스타': '갑각류',
    '랍스터': '갑각류',
    '게살': '갑각류',
    '킹크랩': '갑각류',
    '아몬드': '견과류',
    '피칸': '견과류',
    '헤이즐넛': '견과류',
    '캐슈넛': '견과류',
    '피스타치오': '견과류',
    '잣':'견과류',
    '호두': '견과류',
    '전복': '조개류',
    '굴': '조개류',
    '바지락': '조개류',
    '홍합':'조개류',
    '소라':'조개류',
    '두유': '대두',
    '메밀': '메밀',
    '밀가루': '밀가루',
    '대두': '대두',
    '우유': '우유',
    '치즈': '우유',
    '생크림': '우유',
    '요거트': '우유',
    '모짜렐라': '우유',
    '까르보': '우유',
    '크림':'우유',
    '로제':'우유',
    '마요네즈':'알류',
    '땅콩': '견과류',
    '새알류': '알류',
    '케찹': '토마토',
    '케챱': '토마토',
    '케첩': '토마토',
    '케쳡': '토마토',
    '굴소스': '조개류',
    '우렁': '갑각류'
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

for index, row in data.iterrows():
    if '굴비' in str(row['음식명']):
        for col in column_names[column_names.index(1):]:
            if '조개' in str(row[col]):
                data.at[index, col] = None  # Replace the cell with NaN
    if '가오리' in str(row['음식명']):
        for col in column_names[column_names.index(1):]:
            if '오리고기' in str(row[col]):
                data.at[index, col] = None  # Replace the cell with NaN
    if '고추장' in str(row['음식명']):
        for col in column_names[column_names.index(1):]:
            if '고추' in str(row[col]):
                data.at[index, col] = None  # Replace the cell with NaN


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

bread_list = ["피자", "핫도그", "샌드위치", "토스트", "퀘사디아", "버거", "또띠아", "팬케이크", "파니니", "브리또"]
cnt = False

for index, row in data.iterrows():
    if '빵' == str(row['음식분류2']):
        for bread in bread_list:
            if bread in str(row['음식명']):
                cnt = True
        if cnt is False:
            # print(index)
            data.loc[index] = None
            # data.drop(index)
        cnt = False

# 변경된 데이터 저장
output_path = './preprocessed_data/menu_ingredient/new_ingredient.xlsx'  # 여기에 저장할 파일 경로를 입력해주세요.
data.to_excel(output_path, index=False)