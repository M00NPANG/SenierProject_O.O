import pandas as pd
import tkinter as tk
from PIL import Image, ImageTk
from tkinter import messagebox
import requests
from io import BytesIO

# 처리할 파일 리스트
path ='data/musinsa'

clothes_code = ['10', '11','20', '21','30','40','41','42','43','50','60','61','70','71','72','73']
def process_file(file_path):
    global df, index, root ,img_label
    df = pd.read_csv(file_path)
    index = 0

    # 메인 윈도우 생성
    root = tk.Tk()
    root.title("Image Style Labeler")

    def update_image():
        global index, img_label, img_display
        if index < len(df):
            img_url = df.loc[index, 'cl_photo_path']
            response = requests.get(img_url)
            img_data = response.content
            img = Image.open(BytesIO(img_data))
            img = img.resize((250, 250), Image.Resampling.LANCZOS)
            img = ImageTk.PhotoImage(img)
            img_display = img
            img_label.config(image=img)
            img_label.image = img

    def save_style(style):
        global index
        if style == 'X':
            df.drop(index, inplace=True)
            df.reset_index(drop=True, inplace=True)
        else:
            df.at[index, 'cl_style'] = style
            index += 1

        if index >= len(df):
            messagebox.showinfo("완료", "모든 이미지가 레이블링되었습니다.")
            output_path = file_path.replace('.csv', '_labeled.csv')
            df.to_csv(output_path, index=False)
            root.quit()
        else:
            update_image()

    # GUI 요소 생성
    img_label = tk.Label(root)
    img_label.pack()

    tk.Button(root, text="Feminine", command=lambda: save_style('Feminine')).pack(side=tk.LEFT)
    tk.Button(root, text="Casual", command=lambda: save_style('Casual')).pack(side=tk.LEFT)
    tk.Button(root, text="Street", command=lambda: save_style('Street')).pack(side=tk.LEFT)
    tk.Button(root, text="X", command=lambda: save_style('X')).pack(side=tk.LEFT)

    # 초기 이미지 업데이트
    update_image()

    # GUI 시작
    root.mainloop()

for c in clothes_code:
    process_file(f'{path}/{c}musinsa.csv')
