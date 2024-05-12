from personal_color.src.clothes_personal_color import Clothes
import pandas as pd

clothes_code = ['10', '11','20', '21','30''40','41','42','43','50','60','61','70','71','72','73']
CLOTHES_CODE = 10
def main():
    
    
    
    
    
    clothes_data_url = f"crawling/data/musinsa/{CLOTHES_CODE}musinsa.csv"
    
    colorframe = pd.DataFrame()
    percol_list = []
    color_list = []
    dataframe = pd.read_csv(clothes_data_url)
    for df in dataframe.itertuples():
        url=df.cl_photo_path
        data= Clothes.ColorCheck(url)
        percol_list.append(data[1])
        color_list.append(data[0])
    
    colorframe['cl_personal_color']=percol_list
    colorframe['cl_color']=color_list
    dataframe = pd.concat([dataframe, colorframe], axis=1)

    dataframe.to_csv(clothes_data_url, index=False,encoding='utf-8')


if __name__ == '__main__':
    for i in clothes_code:
        CLOTHES_CODE = i
        main()


