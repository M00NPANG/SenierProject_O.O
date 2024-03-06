package teamProject_Server.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import teamProject_Server.Domain.Color;
import teamProject_Server.Domain.Style;
import teamProject_Server.Domain.User;
import teamProject_Server.Repository.ColorRepository;
import teamProject_Server.Repository.StyleRepository;
import teamProject_Server.Repository.UserRepository;

@Service
public class UserPreferenceService {

    private final UserRepository userRepository;
    private final ColorRepository colorRepository;
    private final StyleRepository styleRepository;

    @Autowired
    public UserPreferenceService(UserRepository userRepository, ColorRepository colorRepository, StyleRepository styleRepository) {
        this.userRepository = userRepository;
        this.colorRepository = colorRepository;
        this.styleRepository = styleRepository;
    }

    // 유저 선호도 업데이트
    public void updateUserPreferences(String userEmail, String userColor, String userStyle) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다"));

        // 유저 선호색 가중치 증가
        Color color = colorRepository.findByUser(user);
        updateColorPreferenceCount(color, userColor);

        // 유저 선호스타일 가중치 증가
        Style style = styleRepository.findByUser(user);
        updateStylePreferenceCount(style, userStyle);
    }


    // 유저 선호색 학습
    private void updateColorPreferenceCount(Color color, String userColor) {
        switch (userColor.toLowerCase()) {
            case "red":
                color.setCol_red(color.getCol_red() + 1);
                break;
            case "orange":
                color.setCol_orange(color.getCol_orange() + 1);
                break;
            case "yellow":
                color.setCol_yellow(color.getCol_yellow() + 1);
            case "green":
                color.setCol_green(color.getCol_green() + 1);
            case "blue":
                color.setCol_blue(color.getCol_blue() + 1);
            case "purple":
                color.setCol_purple(color.getCol_purple() + 1);
            case "brown":
                color.setCol_brown(color.getCol_brown() + 1);
            case "gray":
                color.setCol_gray(color.getCol_gray() + 1);
            default:
                throw new IllegalArgumentException("존재하지 않는 색상입니다.");
        }
        colorRepository.save(color);
    }


    // 유저 선호 스타일 학습
    private void updateStylePreferenceCount(Style style, String userStyle) {
        switch (userStyle.toLowerCase()) {
            case "street":
                style.setSty_street(style.getSty_street() + 1);
                break;
            case "modern":
                style.setSty_modern(style.getSty_modern() + 1);
                break;
            case "minimal":
                style.setSty_minimal(style.getSty_minimal() + 1);
                break;
            case "feminine":
                style.setSty_feminine(style.getSty_feminine() + 1);
                break;
            case "simpleBasic":
                style.setSty_simpleBasic(style.getSty_simpleBasic() + 1);
                break;

             // 추가 예정.... 너무 졸림 ㅎㅎ
            default:
                throw new IllegalArgumentException("존재하지 않는 스타일입니다.");
        }
        styleRepository.save(style);
    }
}

