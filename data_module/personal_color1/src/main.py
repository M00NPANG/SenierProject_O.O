from face import PersonalColor

SEASON = "au_warm"

def main():
    personal_color = PersonalColor(excel_file =f"{SEASON}.xlsx")
    for i in range(1,21,1):
        image_path = f'{SEASON}\{SEASON}{i}.jpg'
        white_image = personal_color.white_balance(image_path)
        result_image = personal_color.face_parsing(white_image)
        personal_color.skin_analysis(result_image, k=4)




if __name__ == '__main__':
    main()