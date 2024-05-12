import pandas as pd

# 주요 11가지 색상과 해당 RGB 값
main_colors = {
    'red': (255, 0, 0),
    'orange': (255, 165, 0),
    'yellow': (255, 255, 0),
    'green': (0, 128, 0),
    'blue': (0, 0, 255),
    'navy': (0, 0, 128),
    'purple': (128, 0, 128),
    'black': (0, 0, 0),
    'white': (255, 255, 255),
    'brown': (165, 42, 42),
    'gray': (128, 128, 128),
    'pink': (255, 105, 180)
}

# RGB 값을 입력받아 가장 가까운 색상 이름을 반환하는 함수
def find_closest_color(rgb):
    min_distance = float('inf')
    closest_color = None
    for color_name, color_rgb in main_colors.items():
        # 각 색상과의 유클리드 거리 계산
        distance = sum((comp - c) ** 2 for comp, c in zip(color_rgb, rgb))
        if distance < min_distance:
            min_distance = distance
            closest_color = color_name
    return closest_color

# color.csv 파일을 불러옵니다.
df = pd.read_csv('src\clothes_personal_color\colors.csv')

# 색상 이름을 'color' 열에 저장
df['color'] = df.apply(lambda x: find_closest_color((x['Red (8 bit)'], x['Green (8 bit)'], x['Blue (8 bit)'])), axis=1)

# 수정된 DataFrame을 다시 CSV로 저장
df.to_csv('src\clothes_personal_color\colors.csv', index=False)
