
import numpy as np
import matplotlib.pyplot as plt
from matplotlib.patches import Rectangle
from skimage.io import imread, imshow, imsave
from skimage import img_as_ubyte

def whitepatch_balancing_save(image, from_row, from_column, row_width, column_width, save_path):
    img_height, img_width = image.shape[:2]

    # 패치 위치와 크기를 안전하게 조정
    from_row = max(0, min(from_row, img_height - row_width))
    from_column = max(0, min(from_column, img_width - column_width))
    row_width = min(row_width, img_height - from_row)
    column_width = min(column_width, img_width - from_column)

    fig, ax = plt.subplots(1, 2, figsize=(10, 5))
    ax[0].imshow(image)
    ax[0].add_patch(Rectangle((from_column, from_row), 
                              column_width, row_width, 
                              linewidth=3, edgecolor='r', facecolor='none'))
    ax[0].set_title('Original Image')

    # 화이트 패치 영역 추출 및 밸런싱 적용
    image_patch = image[from_row:from_row+row_width, from_column:from_column+column_width]
    max_value = image_patch.max(axis=(0, 1))
    if (max_value == 0).all():  # 모든 채널의 최대값이 0인 경우 체크
        print("Error: Patch maximum value is zero, which indicates an empty or invalid patch.")
        return

    image_max = (image * 1.0 / max_value).clip(0, 1)
    ax[1].imshow(image_max)
    ax[1].set_title('Whitebalanced Image')
    plt.tight_layout()
    plt.show()

    balanced_image = img_as_ubyte(image_max)
    imsave(save_path, balanced_image)
    print(f"Saved whitebalanced image to {save_path}")

# 이미지 불러오기 및 함수 호출
dinner = imread('6.jpg')
whitepatch_balancing_save(dinner, 1200, 1200, 150, 150, '1.jpg')
