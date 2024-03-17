import pandas as pd
import matplotlib.pyplot as plt
import seaborn as sns

# 파일 이름과 색상 매핑
files = {
    'sp_warm.xlsx': 'blue',
    'su_cool.xlsx': 'yellow',
    'au_warm.xlsx': 'green',
    'win_cool.xlsx': 'red'
}

# 데이터를 담을 리스트 초기화
data = []

# 각 파일을 순회하면서 데이터 읽기
for file, color in files.items():
    df = pd.read_excel(file)
    file_base = file.split('.')[0]


     # 중앙값 출력
    print(f"{file_base} 파일의 중앙값:")
    print(f"B 중앙값: {df['b'].median()}")
    print(f"H 중앙값: {df['H'].median()}")
    print(f"S 중앙값: {df['S'].median()}\n")
    # 'b' 값
    data.append(pd.DataFrame({'Season': 'B', 'Value': df['b'], 'File': file.split('.')[0], 'Color': color}))
    
    # 'h' 값
    data.append(pd.DataFrame({'Season': 'H', 'Value': df['H'], 'File': file.split('.')[0], 'Color': color}))
    
    # 's' 값
    data.append(pd.DataFrame({'Season': 'S', 'Value': df['S'], 'File': file.split('.')[0], 'Color': color}))

# 모든 데이터를 하나의 DataFrame으로 합치기
combined_data = pd.concat(data)

# 각 Season 별로 플롯을 생성하기 위한 준비
unique_seasons = combined_data['Season'].unique()

# 플롯 생성을 위한 Figure와 Axes 설정
fig, axes = plt.subplots(nrows=1, ncols=len(unique_seasons), figsize=(18, 6), sharey=False)

# 각 Season 별로 별도의 박스플롯 생성 및 값의 범위 설정
value_ranges = {'B': (120, 160), 'H': (0, 20), 'S': (0, 100)}  # 예시 값 범위

for ax, season in zip(axes, unique_seasons):
    sns.boxplot(x='Season', y='Value', hue='File', data=combined_data[combined_data['Season'] == season], palette=files.values(), ax=ax)
    ax.set_title(f'{season} 값의 분포')
    ax.set_xlabel('Value Type')
    ax.set_ylabel('Value')
    ax.legend(loc='upper right', title='File')
    ax.set_ylim(value_ranges[season])  # 각 Season 별로 값의 범위 설정

plt.tight_layout()
plt.show()
