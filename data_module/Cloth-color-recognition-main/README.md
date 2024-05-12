# Cloth Color Recognition

## Dataset Folder Structure
- Folder
  -  CLASS_1/
    [IMAGES]
  -  CLASS_2/
    [IMAGES]
  -  CLASS_3/
    [IMAGES]
  -  .../
    [IMAGES]
    
## TEST DATA
https://www.kaggle.com/dqmonn/zalando-store-crawl/download

## How To Run
1. Download COLORS.csv file for determining colors.
- https://drive.google.com/file/d/1E4EZ-p4chwM4ASxASLgrJo8tLeiOa64U/view?usp=sharing
2. Install requirements with code below:
- ``` pip install -r requirements.txt ```
3. Run main.py. This script determines image's background color.
- ```python main.py -i/--image [IMAGE_PATH] -c/--colors [COLOR_CSV]```

## An Example
Query Image:

<img src="https://github.com/Burak-Tasci/Clothe-color-segmentation/blob/main/images/img_screenshot_17.11.2021.png" width="240">

Result:

<img src="https://github.com/Burak-Tasci/Clothe-color-segmentation/blob/main/images/Figure_1.png" width="240">
