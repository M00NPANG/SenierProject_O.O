package teamProject_Server.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import teamProject_Server.Domain.Clothes;
import teamProject_Server.Service.ClothesService;


import java.util.List;

@RestController
public class ClothesController {
    private final ClothesService clothesService;
    @Autowired
    public ClothesController(ClothesService clothesService) {
        this.clothesService = clothesService;
    }

    @GetMapping("/receiveClothes")
    public List<Clothes> getClothesByEmail(@RequestParam String email) {
        // 이메일을 통해 해당 사용자의 옷 정보를 조회하여 반환
        return clothesService.getClothesByEmail(email);
    }

}
