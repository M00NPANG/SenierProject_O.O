import pandas as pd
import requests
from bs4 import BeautifulSoup as bs
import time
from tqdm import tqdm
from selenium import webdriver

main_cate_code = ["001","003","020","018","005","025","054","007"]
sub_cate_code= [
    ['001010', '001011%2C001001'],
    ['003009', '003002%2C003007%2C003008%2C003004%2C003005'],
    [''],
    [''],
    ['005004','005014','005011'],
    [''],
    ['054006','054003%2C054001%2C054002'],
    ['007001','007005','007004','007002']
]
clothes_code = [
    ['10', '11'],
    ['20', '21'],
    ['30'],
    ['40'],
    ['41','42','43'],
    ['50'],
    ['60','61'],
    ['70','71','72','73']
]
#상의 001
# 긴팔 티셔츠           001010                 10
# 반팔 티셔츠,민소매    001011%2C001001         11
#하의 003
# 반바지  003009 20
# 긴 바지 003002%2C003007%2C003008%2C003004%2C003005  21
#원피스 020
# 단벌옷 none  30
#신발 018
# 운동화 캔버스 none  40
#신발 005
# 샌들 005004 41
# 구두 005014 42
# 부츠 005011 43
#악세사리 025
# 주얼리 none 50
#가방 054 
# 백팩 054006 60
# 핸드백 054003%2C054001%2C054002 61
#모자 007
# 캡모자 007001 70
# 비니  007005 71
# 버킷 007004 72
# 베레모 007002 73
CLOTHES_CODE ="30"
def main(mainCategory,subCategory):
    
    url =f"https://www.musinsa.com/ranking/best?period=now&age=ALL&mainCategory={mainCategory}&subCategory={subCategory}&leafCategory=&price=&golf=false&kids=false&newProduct=false&exclusive=false&discount=false&soldOut=false&page=1&viewType=small&priceMin=&priceMax="
    final_df=musinsa_crawling(url)
    file_name = f"data\musinsa\{CLOTHES_CODE}musinsa.csv"
    final_df.to_csv(file_name, index=False,encoding='utf-8')

def musinsa_crawling(url):
    #기본 url
    #url = "https://www.musinsa.com/ranking/best?period=now&age=ALL&mainCategory=001&subCategory=&leafCategory=&price=&golf=false&kids=false&newProduct=false&exclusive=false&discount=false&soldOut=false&page=1&viewType=small&includeMen=false&priceMin=&priceMax="
    
    response = requests.get(url)
    html = bs(response.text, 'lxml')
    musinsa_df = rbnl(html)
    return musinsa_df

    

def rbnl(html):
    musinsa_rank_df = pd.DataFrame()

    #링크와 의류명뽑기
    link_name_html = html.select('#goodsRankList > li > div.li_inner > div.article_info > p.list_info > a')
    link_list = []
   
    for i in link_name_html:
        link_list.append(i['href'])
       


    musinsa_rank_df['cl_url'] = link_list
   #상세 페이지 크롤링
    musinsa_rank_df2 = specific_info(link_list)
    
    #데이터 프레임 옆으로 합치기
    musinsa_rank_df = pd.concat([musinsa_rank_df, musinsa_rank_df2], axis=1)
    musinsa_rank_df['user_id'] = 1
    musinsa_rank_df['cl_category'] = CLOTHES_CODE
    return musinsa_rank_df

def specific_info(link_list):
    headers={'User-Agent':'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/101.0.4951.67 Safari/537.36'}
    
    musinsa_rank_df = pd.DataFrame()

    img_list =[]
    
    for link in tqdm(link_list):
#     
         
        #셀레니움으로 원하는 데이터 가져오기
        driver = webdriver.Chrome()
        driver.get(link)

        driver.implicitly_wait(10)

        sel_html=driver.page_source   
        html_2=bs(sel_html,"lxml")


        
       
        img_tag = html_2.find("img", {"class":"product-detail__sc-p62agb-2 cHoXWh"})
        if img_tag:
            img_src = img_tag.get("src")
            img_list.append(img_src)
        else:
            img_list.append("이미지 없음")
        
        #
        
        #드라이버 닫아주기
        driver.close()
       
        #시간 추가
        time.sleep(0.01)

    musinsa_rank_df['cl_photo_path']=img_list
    return musinsa_rank_df






if __name__ == '__main__':
    for idx, main_code in enumerate(main_cate_code):
        sub_list = sub_cate_code[idx]
        clothes_list = clothes_code[idx]
        for i,s in enumerate(sub_list):
            CLOTHES_CODE=clothes_code[idx][i]
            main(main_code, s)
 


    