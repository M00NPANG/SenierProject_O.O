import cv2
import numpy as np
import pandas as pd

# Colors data file
class ColorRecogniser:
    """docstring for Color"""
    def __init__(self, colors_csv):
        super(ColorRecogniser, self).__init__()
        features=["Name","Red (8 bit)","Green (8 bit)","Blue (8 bit)"]
        self.colors_data = pd.read_csv(colors_csv)[features]
        
    def getColorName(self,B,G,R):
        # including colors_csv
        # minimum variable for finding the closest color
        minimum = 1000
        # testing colors one by one
        for i in range(len(self.colors_data)):
            d = abs(R- int(self.colors_data.loc[i,"Red (8 bit)"])) + abs(G- int(self.colors_data.loc[i,"Green (8 bit)"]))+ abs(B- int(self.colors_data.loc[i,"Blue (8 bit)"]))
            if(d<minimum):
                minimum = d
                color_name = self.colors_data.loc[i,"Name"]
        return color_name

    def getColors(self,img):
        pixels = np.float32(img.reshape(-1, 3))
        n_colors = 2
        criteria = (cv2.TERM_CRITERIA_EPS + cv2.TERM_CRITERIA_MAX_ITER, 200, .1)
        flags = cv2.KMEANS_RANDOM_CENTERS
        _, labels, palette = cv2.kmeans(pixels, n_colors, None, criteria, 10, flags)

        return palette