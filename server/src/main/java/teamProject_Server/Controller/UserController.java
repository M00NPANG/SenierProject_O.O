package teamProject_Server.Controller;

import org.springframework.beans.factory.annotation.Autowired;
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


    // 회원가입 1 -> 이메일, 비밀번호, 이름 전달해주면 됨!!!
    @PostMapping("/join")
    public String join(@RequestBody User user) {
        // 회원 정보를 저장하고 저장된 회원의 email 반환
        return userService.join(user);
    }

   // 회원가입 2

    @PostMapping("/login")
    public String login(@RequestBody User user) {

        boolean success = userService.login(user.getEmail(), user.getPassword());

        if (success) {
            return "로그인 성공";
        } else {
            return "로그인 실패";
        }
    }

}
