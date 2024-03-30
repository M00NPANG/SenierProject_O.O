import pandas as pd
import requests
from bs4 import BeautifulSoup as bs
import time
from tqdm import tqdm
from selenium import webdriver


def musinsa_crawling():
    url = "https://www.musinsa.com/ranking/best?period=now&age=ALL&mainCategory=001&subCategory=&leafCategory=&price=&golf=false&kids=false&newProduct=false&exclusive=false&discount=false&soldOut=false&page=1&viewType=small&includeMen=false&priceMin=&priceMax="
    response = requests.get(url)
    html = bs(response.text, 'lxml')
    musinsa_df = rbnl(html)
    musinsa_df2 = driverUse(url)
    musinsa_df = pd.concat([musinsa_df, musinsa_df2], axis=1)
    return musinsa_df


def driverUse(url):
    musinsa_rank_df = pd.DataFrame()
    
    driver = webdriver.Chrome()
    driver.get(url)
    
    driver.implicitly_wait(10)
    
    sel_html=driver.page_source
    html_2=bs(sel_html,"lxml")
    
    #조회수 가져오기
    
    img_html=html_2.find_all("img", {"class":"lazyload lazy"})
    img_list = []
    for i in img_html:
        img_list.append(i['data-original'])
    
    
    
   
    
    #드라이버 닫아주기
    driver.close()
    
    
    musinsa_rank_df['이미지URL'] = img_list
    
    return musinsa_rank_df
    

def rbnl(html):
    musinsa_rank_df = pd.DataFrame()
    
    
    #브랜드 이름 뽑기
    brand_html = html.select('#goodsRankList > li > div.li_inner > div.article_info > p.item_title > a')
    brand_list = []
    
    for i in brand_html:
        brand_list.append(i.string)
        
    musinsa_rank_df['브랜드명']=brand_list
    
    #의류명 뽑기
    link_name_html = html.select('#goodsRankList > li > div.li_inner > div.article_info > p.list_info > a')
    name_list = []
    
    for i in link_name_html:
        name_list.append(i['title'])
        
    musinsa_rank_df['의류명']=name_list
    
    
    
    #가격 뽑기
    price_html = html.select('#goodsRankList > li > div.li_inner > div.article_info > p.price')
    price_list =[]
    
    for i in price_html:
        del_tag =i.find('del')
        if del_tag:
            price_list.append(del_tag.get_text(strip=True))
        else:
            price_list.append(i.string.strip())
    musinsa_rank_df['가격'] = price_list
    
    
    return musinsa_rank_df





final_df=musinsa_crawling()


file_name = "musinsa.csv"
final_df.to_csv(file_name, index=False,encoding='utf-8')
pd.read_csv(file_name,encoding='utf-8')
    