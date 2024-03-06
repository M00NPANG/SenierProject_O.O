package teamProject_Server.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import teamProject_Server.Domain.User;
import teamProject_Server.Service.UserService;

@RestController
@RequestMapping("/api")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/test")
    public String testEndPoint() { return "Test end point is working" ; }


    // 회원가입
    @PostMapping("/join")
    public ResponseEntity<String> join(@RequestBody User user) {
        try {
            // 회원 정보를 저장하고 저장된 회원의 email 반환
            String userEmail = userService.join(user);
            return ResponseEntity.ok(userEmail);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

   // 로그인
    @PostMapping("/login")
    public String login(@RequestBody User user) {

        boolean success = userService.login(user.getEmail(), user.getPassword());

        if (success) {
            return "로그인 성공";
        } else {
            return "로그인 실패";
        }
    }

    // 비밀번호 변경
    @PostMapping("/editPassword")
    public ResponseEntity<String> editPassword(@RequestParam String email, @RequestParam String name, @RequestParam String newPassword) {
        try {
            // 유저 확인 서비스를 사용하여 유저 신원 확인
            User user = userService.verifyUser(email, name);
            // 확인된 유저를 사용하여 비밀번호 변경
            userService.editPassword(user, newPassword);
            return ResponseEntity.ok("비밀번호가 성공적으로 변경되었습니다.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
