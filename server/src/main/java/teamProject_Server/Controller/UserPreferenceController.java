package teamProject_Server.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import teamProject_Server.Service.UserPreferenceService;

@RestController
@RequestMapping("/api/preferences")
public class UserPreferenceController {
    private final UserPreferenceService userPreferenceService;

    @Autowired
    public UserPreferenceController(UserPreferenceService userPreferenceService) {
        this.userPreferenceService = userPreferenceService;
    }

    // 사용자 선호도 업데이트
    @PostMapping("/update")
    public ResponseEntity<String> updateUserPreferences(@RequestParam String userEmail, @RequestParam String userColor, @RequestParam String userStyle) {
        try {
            userPreferenceService.updateUserPreferences(userEmail, userColor, userStyle);
            return ResponseEntity.ok("사용자의 선호도가 업데이트되었습니다.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}