package teamProject_Server.DTO;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserPreferenceUpdateRequest {
    private String userEmail;
    private String userColor;
    private String[] userStyles;     // 스타일 배열로 받음
    private Long colorNum;
    @Getter
    private Long[] styleNum;        // 스타일 점수 배열로 받음

    public String[] getUserStyle() {
        return userStyles;
    }

}
