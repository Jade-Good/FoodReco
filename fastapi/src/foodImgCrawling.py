
import os

import pandas as pd
from selenium.webdriver.common.keys import Keys
import time
from selenium import webdriver
from webdriver_manager.chrome import ChromeDriverManager
from selenium.webdriver.common.by import By
from selenium.webdriver.chrome.service import Service
import urllib.request
import boto3

s3 = boto3.client('s3',
                  aws_access_key_id='???',
                  aws_secret_access_key='???')

chrome_options = webdriver.ChromeOptions()
driver = webdriver.Chrome(service=Service(ChromeDriverManager().install()),options=chrome_options)


excel_file = 'C:/Users/SSAFY/Desktop/foodDB.xlsx'  # 음식 이름이 포함된 엑셀 파일 경로
df = pd.read_excel(excel_file, sheet_name='Sheet1')  # 시트 이름은 필요에 따라 수정하세요


for index, row in df.iterrows():
    URL = 'http://www.google.co.kr/imghp'
    driver.get(url=URL)
    driver.implicitly_wait(time_to_wait=10)
    food_name = row['음식명']  # CSV 파일에서 컬럼 이름에 맞게 수정
    keyElement = driver.find_element(By.XPATH, '//*[@id="APjFqb"]')

    # 검색어 입력 및 검색 실행
    keyElement.clear()
    keyElement.send_keys(food_name)
    keyElement.send_keys(Keys.RETURN)

    # 첫 번째 이미지 URL 가져오기
    time.sleep(3)  # 이미지 로딩 대기 시간 (조절 필요)
    try:
        url = driver.find_element(By.CSS_SELECTOR,
            '#islrg > div.islrc > div:nth-child(2) > a.FRuiCf.islib.nfEiy > div.fR600b.islir > img').get_attribute(
            'src')

        # 이미지 다운로드
        image_name = "C:/Users/SSAFY/foodImg/" + f"{food_name}.jpg"
        urllib.request.urlretrieve(url, image_name)

        # 이미지를 S3에 업로드
        s3.upload_file(image_name, 'food-reco', f"foodImg/{index+1}.jpg")

        # 로컬 이미지 파일 삭제
        os.remove(image_name)

        print(f"Image for {food_name} uploaded to S3")
    except Exception as e:
        print(f"Error uploading image for {food_name}: {str(e)}")

# 크롬 드라이버 종료
driver.quit()