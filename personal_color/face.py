import cv2
from rembg import remove
from PIL import Image
import numpy as np
import io
from sklearn.cluster import KMeans
import pandas as pd
import os
import sys
SEASON = "au_warm"


class PersonalColor:

    def __init__(self, excel_file=f'{SEASON}.xlsx'):
        # Excel 파일 이름을 초기화
        self.excel_file = excel_file



        #HSV값을 파일에 저장
    def add_data(self, L, a, b, H, S, V):
        # Excel 파일이 존재하는지 확인하고, 존재하지 않으면 새 DataFrame을 생성
        try:
            df = pd.read_excel(self.excel_file)
        except FileNotFoundError:
            df = pd.DataFrame(columns=['Image', 'L', 'a', 'b', 'H', 'S', 'V'])
    
        # 새로운 행을 생성 (Lab과 HSV 값 모두 포함)
        new_row = pd.DataFrame({
            'L': [L], 
            'a': [a], 
            'b': [b],
            'H': [H], 
            'S': [S], 
            'V': [V]
        })
    
        # 새로운 행을 기존 DataFrame에 추가
        df = pd.concat([df, new_row], ignore_index=True)
    
        # DataFrame을 Excel 파일로 저장
        df.to_excel(self.excel_file, index=False, engine='openpyxl')
        print(f"값이 {self.excel_file}에 저장됨")

    def face_parsing(self, image):
        # 이미지를 로드
        image_np = np.array(image)

        # 얼굴 감지를 위한 Haar Cascade 분류기를 로드
        face_cascade = cv2.CascadeClassifier(cv2.data.haarcascades + 'haarcascade_frontalface_default.xml')

        # 그레이스케일 이미지로 변환하여 얼굴을 감지
        gray_image = cv2.cvtColor(image_np, cv2.COLOR_BGR2GRAY)
        faces = face_cascade.detectMultiScale(gray_image, scaleFactor=1.1, minNeighbors=5, minSize=(30, 30))

        # 얼굴 영역이 감지된 경우에만 처리
        if len(faces) > 0:
            x, y, w, h = faces[0]
            # 얼굴 영역만 추출
            face_image = image_np[y:y+h, x:x+w]

            # 이미지를 바이트 배열로 변환
            _, buffer = cv2.imencode('.png', face_image)
            image_bytes = buffer.tobytes()

            # 배경 제거 (rembg 사용)
            result_bytes = remove(image_bytes)

            # 결과 이미지를 PIL Image로 변환하여 반환
            result_image = Image.open(io.BytesIO(result_bytes))
            return result_image
        else:
            print("얼굴을 찾을 수 없음.")
            return None




    def skin_analysis(self, image, k=4):
        if image is None:
            return None

        # 이미지를 Lab 색공간으로 변환
        image_cv = cv2.cvtColor(np.array(image), cv2.COLOR_RGBA2BGRA)
        lab_image = cv2.cvtColor(image_cv, cv2.COLOR_BGR2Lab)
    
        # 동시에 이미지를 HSV 색공간으로 변환
        hsv_image = cv2.cvtColor(image_cv, cv2.COLOR_BGR2HSV)
    
        # 알파 채널을 사용하여 투명하지 않은 부분의 마스크를 생성
        alpha_channel = image_cv[:, :, 3]
        non_transparent_mask = alpha_channel > 0

        # 투명하지 않은 부분의 Lab 값만 추출
        lab_values_non_transparent = lab_image[non_transparent_mask]
    
        # 투명하지 않은 부분의 HSV 값만 추출
        hsv_values_non_transparent = hsv_image[non_transparent_mask]
    
        # KMeans 클러스터링을 Lab 색공간에서 수행
        clt_lab = KMeans(n_clusters=k)
        clt_lab.fit(lab_values_non_transparent)

        # KMeans 클러스터링을 HSV 색공간에서도 수행
        clt_hsv = KMeans(n_clusters=k)
        clt_hsv.fit(hsv_values_non_transparent)

        # Lab 색공간에서 가장 많은 비율을 차지하는 클러스터의 색상 찾기
        hist_lab = np.histogram(clt_lab.labels_, bins=np.arange(0, len(np.unique(clt_lab.labels_)) + 1))
        hist_lab = hist_lab[0].astype("float")
        hist_lab /= hist_lab.sum()
        dominant_lab_index = np.argmax(hist_lab)
        dominant_lab = clt_lab.cluster_centers_[dominant_lab_index]

        # HSV 색공간에서 가장 많은 비율을 차지하는 클러스터의 색상 찾기
        hist_hsv = np.histogram(clt_hsv.labels_, bins=np.arange(0, len(np.unique(clt_hsv.labels_)) + 1))
        hist_hsv = hist_hsv[0].astype("float")
        hist_hsv /= hist_hsv.sum()
        dominant_hsv_index = np.argmax(hist_hsv)
        dominant_hsv = clt_hsv.cluster_centers_[dominant_hsv_index]

        # Lab과 HSV의 dominant 값 추출
        L, a, b = dominant_lab
        H, S, V = dominant_hsv

        # 피부 톤 체크 로직과 데이터 추가 로직은 이 정보를 사용하여 구현
        season=self.skin_tone_check((L, a, b),(H, S, V))  # Lab에 대한 피부 톤 체크
        
        print(season)
        # Lab과 HSV 값을 데이터베이스에 추가하는 로직 구현
        #self.add_data( L, a, b, H, S, V)


   

        
 
    def is_warm(self,lab_b, a):
        '''
        파라미터 lab_b = [skin_b, hair_b, eye_b]
        a = 가중치 [skin, hair, eye]
        질의색상 lab_b값에서 warm의 lab_b, cool의 lab_b값 간의 거리를
        각각 계산하여 warm이 가까우면 1, 반대 경우 0 리턴
        '''
        # standard of skin, eyebrow, eye
        warm_b_std = [128.59962, 0,0]
        cool_b_std = [133.92617, 0, 0]
        
        warm_dist = 0
        cool_dist = 0

        body_part = ['skin', 'eyebrow', 'eye']
        for i in range(1):
            warm_dist += abs(lab_b[i] - warm_b_std[i]) * a[i]
            #print(body_part[i],"의 warm 기준값과의 거리")
            #print(abs(lab_b[i] - warm_b_std[i]))
            cool_dist += abs(lab_b[i] - cool_b_std[i]) * a[i]
            #print(body_part[i],"의 cool 기준값과의 거리")
            #print(abs(lab_b[i] - cool_b_std[i]))
        if(warm_dist <= cool_dist):
            return 1 #warm
        else:
            return 0 #cool

    def is_spr(self,hsv_s, a):
        '''
        파라미터 hsv_s = [skin_s, hair_s, eye_s]
        a = 가중치 [skin, hair, eye]
        질의색상 hsv_s값에서 spring의 hsv_s, fall의 hsv_s값 간의 거리를
        각각 계산하여 spring이 가까우면 1, 반대 경우 0 리턴
        '''
        #skin, hair, eye
        spr_s_std = [63.40508, 0, 0]
        fal_s_std = [72.91237, 0, 0]

        spr_dist = 0
        fal_dist = 0

        body_part = ['skin', 'eyebrow', 'eye']
        for i in range(1):
            spr_dist += abs(hsv_s[i] - spr_s_std[i]) * a[i]
            #print(body_part[i],"의 spring 기준값과의 거리")
            #print(abs(hsv_s[i] - spr_s_std[i]) * a[i])
            fal_dist += abs(hsv_s[i] - fal_s_std[i]) * a[i]
            #print(body_part[i],"의 fall 기준값과의 거리")
            #print(abs(hsv_s[i] - fal_s_std[i]) * a[i])

        if(spr_dist <= fal_dist):
            return 1 #spring
        else:
            return 0 #fall

    def is_smr(self,hsv_s, a):
        '''
        파라미터 hsv_s = [skin_s, hair_s, eye_s]
        a = 가중치 [skin, hair, eye]
        질의색상 hsv_s값에서 summer의 hsv_s, winter의 hsv_s값 간의 거리를
        각각 계산하여 summer가 가까우면 1, 반대 경우 0 리턴
        '''
        #skin, eyebrow, eye
        smr_s_std = [39.91783, 0, 0]
        wnt_s_std = [53.62602, 0, 0]
        a[1] = 0.5 # eyebrow 영향력 적기 때문에 가중치 줄임

        smr_dist = 0
        wnt_dist = 0

        body_part = ['skin', 'eyebrow', 'eye']
        for i in range(1):
            smr_dist += abs(hsv_s[i] - smr_s_std[i]) * a[i]
            #print(body_part[i],"의 summer 기준값과의 거리")
            #print(abs(hsv_s[i] - smr_s_std[i]) * a[i])
            wnt_dist += abs(hsv_s[i] - wnt_s_std[i]) * a[i]
            #print(body_part[i],"의 winter 기준값과의 거리")
            #print(abs(hsv_s[i] - wnt_s_std[i]) * a[i])

        if(smr_dist <= wnt_dist):
            return 1 #summer
        else:
            return 0 #winter     
   
    def skin_tone_check(self, lab_value,hsv_value):
        season = ""
        v = [1,0,0]

        L = [0,0,0]
        a = [0,0,0]
        b = [0,0,0]

        H = [0,0,0]
        S = [0,0,0]
        V = [0,0,0]

        L[0], a[0], b[0] = lab_value
        H[0], S[0], V[0] = hsv_value

        if(self.is_warm(b,v)):
            if(self.is_spr(b,v)):
                season ="봄 웜"
            else:
                season ="가을 웜"
        else:
            if(self.is_smr(b,v)):
                season ="가을 쿨"
            else:
                season ="겨울 쿨"
        
        return season
    

    def white_balance(self,image_path):
        # 이미지를 불러옵니다.
        img = cv2.imread(image_path)
    
        # 이미지를 HSV 색상 공간으로 변환합니다.
        hsv_img = cv2.cvtColor(img, cv2.COLOR_BGR2HSV)
    
        # 피부색에 해당하는 HSV 범위를 정의합니다.
        lower_skin = np.array([0, 48, 80], dtype=np.uint8)
        upper_skin = np.array([20, 255, 255], dtype=np.uint8)
    
        # 피부색 영역을 마스킹합니다.
        skin_mask = cv2.inRange(hsv_img, lower_skin, upper_skin)
    
        # 마스크를 사용하여 피부 영역의 HSV 값을 추출합니다.
        skin_hsv = cv2.bitwise_and(hsv_img, hsv_img, mask=skin_mask)
        skin_v = skin_hsv[:,:,2][skin_mask == 255]
    
        # 피부 영역의 평균 V(명도) 값을 계산합니다.
        if skin_v.size == 0:
            print("No skin area found.")
            return
        average_v = np.mean(skin_v)
    
        # 전체 이미지의 V(명도) 값을 조정합니다.
        # 여기서는 전체 이미지의 명도를 피부 영역의 평균 명도에 맞추어 조정합니다.
        target_v = 150  # 목표 명도 값
        v_ratio = target_v / average_v
        hsv_img[:,:,2] = np.clip(hsv_img[:,:,2] * v_ratio, 0, 255)
    
        # 조정된 HSV 이미지를 BGR 색상 공간으로 다시 변환합니다.
        adjusted_img = cv2.cvtColor(hsv_img, cv2.COLOR_HSV2BGR)
    
        # 결과 이미지를 반환.
        result_image = Image.fromarray(adjusted_img)
        return result_image
        
# 사용 예시
if __name__ == "__main__":
    personal_color = PersonalColor()
    image_path = sys.argv[1]
    white_image = personal_color.white_balance(image_path)
    result_image = personal_color.face_parsing(white_image)
    season=personal_color.skin_analysis(result_image, k=4)
    



