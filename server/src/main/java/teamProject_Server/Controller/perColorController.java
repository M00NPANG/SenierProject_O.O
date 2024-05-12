package teamProject_Server.Controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import teamProject_Server.DataModule.PersonalColor;


import java.io.IOException;

@RestController
@RequestMapping("api/percol")
public class perColorController {


    private PersonalColor personalColor;

    @PostMapping("/per")
    public String uploadFile() {
        try {
            return PersonalColor.personal("../personal_color/res/win_cool/win_cool1.jpg");
        } catch (IOException e) {
            // 적절한 예외 처리
            return "File upload failed: " + e.getMessage();
        }
    }
}
