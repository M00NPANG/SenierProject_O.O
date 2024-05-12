import cv2
import pandas as pd
from Color import ColorRecogniser
import argparse
import numpy as np
import matplotlib.pyplot as plt
import os

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

def main(img_url):
    # Argument Parser for CLI
    # ap = argparse.ArgumentParser()
    # ap.add_argument("--image", "-i", required=True,
    # 								help="Image")

    # args = vars(ap.parse_args())

    # Image's Colors
    ImagesAndColors = pd.read_csv("colors.csv")
    Colors = ImagesAndColors["Name"]
    
    # Taking borders and image
    img = cv2.imread(img_url)
    #cv2.imshow("img",img)
    top_border = img[:1,:]
    left_border = img[:,:1]
    right_border = img[:,-2:-1]

    image_colors = []
    bg_colors = []


    recogniser = ColorRecogniser("colors.csv")

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
    print(cloth_color)
    color_df = ImagesAndColors[ImagesAndColors["Name"] == cloth_color]
    print(color_df['percol'].iloc[0])
    sub_percol = color_df['sub_percol'].iloc[0]
    
    if sub_percol is  None:
         print(sub_percol)
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



if __name__ == '__main__':
    dirpath = "images/zalando/hoodies-female"
    imgs = os.listdir(dirpath)
    for imgpath in imgs:
        #print(os.path.join(dirpath, imgpath))
        main(os.path.join(dirpath, imgpath))
    