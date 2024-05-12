import os
import pandas as pd
import requests
FILENUMBER = 1001

excel_file_path = f'.\clothes/{FILENUMBER}musinsa.xlsx'


# 이미지 URL이 포함된 컬럼 이름
image_url_column = 'img_url'

# 이미지를 저장할 폴더
save_folder = f'.\data\musinsa\{FILENUMBER}'
os.makedirs(save_folder, exist_ok=True)


# Excel 파일 읽기
df = pd.read_excel(excel_file_path)

# 이미지 URL 컬럼에서 모든 이미지 URL을 반복하여 다운로드
for image_url in df[image_url_column]:
    # 파일 이름 설정 (URL의 마지막 부분을 파일 이름으로 사용)
    file_name = os.path.join(save_folder, image_url.split('/')[-1])

    # 이미지 다운로드 및 저장
    response = requests.get(image_url)
    if response.status_code == 200:
        with open(file_name, 'wb') as file:
            file.write(response.content)
        print(f"Image successfully downloaded and saved to {file_name}")
    else:
        print(f"Failed to download image from {image_url}")