## 전처리 순서

    1. make_clean_ingredient
        - 의미있는 재료만 선별
    2. replace_string
        - '음식명' 열에 존재하는 재료 추출 및 '음식분류2'열 이후에 중복없이 추가
    3. reordering_ingredient
        - 재료 순서 정렬
    4. k_means_clustering
        - k개의 군집으로 클러스터링
        - 가중치 설정가능
        - k설정 가능
