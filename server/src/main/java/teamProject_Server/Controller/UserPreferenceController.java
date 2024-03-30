package teamProject_Server.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import teamProject_Server.Service.UserPreferenceService;
import teamProject_Server.UserPreferenceUpdateRequest;

import java.util.Map;

@RestController
@RequestMapping("/api/preferences")
public class UserPreferenceController {
    private final UserPreferenceService userPreferenceService;

    @Autowired
    public UserPreferenceController(UserPreferenceService userPreferenceService) {
        this.userPreferenceService = userPreferenceService;
    }

    // 사용자 선호도 업데이트 (유저 이메일과 선호색, 스타일, 각 수치를 입력받음)
    // 수정 필요
    @PostMapping("/update")
    public ResponseEntity<String> updateUserPreferences(@RequestBody UserPreferenceUpdateRequest request) {
        try {
            String userEmail = request.getUserEmail();
            String userColor = request.getUserColor();
            String userStyle = request.getUserStyle();
            Long colorNum = request.getColorNum();
            Long styleNum = request.getStyleNum();

            Map<String, Double> resultMap = userPreferenceService.updateUserPreferences(userEmail, userColor, userStyle, colorNum, styleNum);

            return ResponseEntity.ok("사용자의 선호도가 업데이트되었습니다 : " + resultMap);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}