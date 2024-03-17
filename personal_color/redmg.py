from rembg import remove
from PIL import Image
import io

def remove_background(input_image_path, output_image_path):
    # 입력 이미지 파일을 엽니다.
    with open(input_image_path, 'rb') as input_file:
        input_data = input_file.read()

    # rembg를 사용하여 배경을 제거합니다.
    output_data = remove(input_data)

    # 결과 이미지 데이터를 PIL Image 객체로 변환합니다.
    output_image = Image.open(io.BytesIO(output_data))

    # 결과 이미지를 파일로 저장합니다.
    output_image.save(output_image_path)

# 사용 예시
input_image_path = 'image1.jpg'  # 배경을 제거할 이미지 파일 경로
output_image_path = 'image1_.png'  # 결과 이미지를 저장할 파일 경로

remove_background(input_image_path, output_image_path)

