package teamProject_Server;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserPreferenceUpdateRequest {
    private String userEmail;
    private String userColor;
    private String userStyle;
    private Long colorNum;
    private Long styleNum;

}
