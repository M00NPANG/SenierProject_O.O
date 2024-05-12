import cv2
import pandas as pd
from .Color import ColorRecogniser
import argparse
import numpy as np
import matplotlib.pyplot as plt
import os
import urllib.request



# Returns synced  color
def getSyncColor(image_colors,bg_colors):
    for im in image_colors:
        for bg in bg_colors:
            if im == bg:
                return im

# Returns diff between image_colors and synced color
def diff(sync_color, image_colors):
    for i in image_colors:
        if i != sync_color:
            return i

def ColorCheck(url):
    # 현재 파일의 디렉터리 경로를 얻음
    current_dir = os.path.dirname(__file__)
    # 상대 경로를 사용해 CSV 파일의 전체 경로 생성
    color_csv_path = os.path.join(current_dir, 'colors.csv')
    
    ImagesAndColors = pd.read_csv(color_csv_path)
    Colors = ImagesAndColors["Name"]
    
    resp = urllib.request.urlopen(url)
    image_data = resp.read()
    image_array = np.asarray(bytearray(image_data), dtype=np.uint8)

    # 이미지 디코딩
    img = cv2.imdecode(image_array, cv2.IMREAD_COLOR)

    
    #cv2.imshow("img",img)
    top_border = img[:1,:]
    left_border = img[:,:1]
    right_border = img[:,-2:-1]

    image_colors = []
    bg_colors = []



    recogniser = ColorRecogniser(color_csv_path)

    # LEFT BORDER
    dominants = recogniser.getColors(left_border)
    b,g,r = dominants[0]
    bg_colors.append(recogniser.getColorName(b,g,r))
    b,g,r = dominants[1]
    bg_colors.append(recogniser.getColorName(b,g,r))

    # RIGHT BORDER
    dominants = recogniser.getColors(right_border)
    b,g,r = dominants[0]
    bg_colors.append(recogniser.getColorName(b,g,r))
    b,g,r = dominants[1]
    bg_colors.append(recogniser.getColorName(b,g,r))

    # TOP BORDER
    dominants = recogniser.getColors(top_border)
    b,g,r = dominants[0]
    bg_colors.append(recogniser.getColorName(b,g,r))
    b,g,r = dominants[1]
    bg_colors.append(recogniser.getColorName(b,g,r))


    # IMAGE
    dominants = recogniser.getColors(img)
    b,g,r = dominants[0]
    image_colors.append(recogniser.getColorName(b,g,r))
    b,g,r = dominants[1]
    image_colors.append(recogniser.getColorName(b,g,r))

    # printing synced color
    bg = getSyncColor(image_colors, bg_colors)
    cloth_color = diff(bg,image_colors)
   
    color_df = ImagesAndColors[ImagesAndColors["Name"] == cloth_color]
    per_col=color_df['percol'].iloc[0]
    color=color_df['color'].iloc[0]
    data = [color,per_col]
   
    print(data[0])
    print(data[1])
    ##### PLOTTING


    

    #rgb = color_df.loc[:,["Red (8 bit)","Green (8 bit)","Blue (8 bit)"]]

    # color = np.ones([256,256,3], dtype=np.uint8) * rgb.loc[:,["Red (8 bit)","Green (8 bit)","Blue (8 bit)"]].values
    # plt.title("CLOTHE COLOR")
    # plt.xticks([])
    # plt.yticks([])
    # plt.imshow(color)
    # plt.show()

    # cv2.waitKey(0)
    # cv2.destroyAllWindows()
    return data



if __name__ == '__main__':
    # Argument Parser for CLI
    # ap = argparse.ArgumentParser()
    # ap.add_argument("--image", "-i", required=True,
    #  								help="Image")

    # args = vars(ap.parse_args())
    # Taking borders and image
    #img = args["image"]
    img ="https://image.msscdn.net/images/goods_img/20240305/3921829/3921829_17097755626488_500.jpg"
    # Image's Colors
    ColorCheck(img)
    