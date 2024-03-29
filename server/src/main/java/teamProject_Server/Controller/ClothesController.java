package teamProject_Server.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import teamProject_Server.Domain.Clothes;
import teamProject_Server.Domain.User;
import teamProject_Server.Service.ClothesService;
import teamProject_Server.Service.ImageService;
import teamProject_Server.Service.UserService;

import java.util.List;

@RestController
@RequestMapping("/api/clothes")
public class ClothesController {

    private final ClothesService clothesService;
    private final UserService userService;
    private final ImageService imageService;

    @Autowired
    public ClothesController(ClothesService clothesService, UserService userService, ImageService imageService) {
        this.clothesService = clothesService;
        this.userService = userService;
        this.imageService = imageService;
    }

    @PostMapping("/upload/{userEmail}")
    public ResponseEntity<String> saveClothes(@PathVariable String userEmail,
                                              @RequestParam("category") Long category,
                                              @RequestParam("brand") String brand,
                                              @RequestParam("name") String name,
                                              @RequestParam("price") Long price,
                                              @RequestParam("personalColor") String personalColor,
                                              @RequestParam("file") MultipartFile file) {
        try {
            User user = userService.getUserByEmail(userEmail); // Assume this method exists in UserService
            String photoPath = imageService.saveImage(userEmail, "clothes", file);
            Clothes clothes = new Clothes(null, user, category, brand, name, price, photoPath, personalColor);
            clothesService.saveClothes(clothes);
            return ResponseEntity.ok("Clothes saved successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error saving clothes: " + e.getMessage());
        }
    }

    // 사용자의 모든 옷 목록 조회
    @GetMapping("/get/{userEmail}")
    public ResponseEntity<List<Clothes>> getAllClothesByUser(@PathVariable String userEmail) {
        List<Clothes> clothesList = clothesService.findAllClothesByUserEmail(userEmail);
        return ResponseEntity.ok(clothesList);
    }

    // 큰 카테고리 목록 조회
    @GetMapping("/get/{userEmail}/Hundred/{category}")
    public ResponseEntity<List<Clothes>> getClothesByCategoryHundred(@PathVariable String userEmail, @PathVariable Long category) {
        List<Clothes> clothesList = clothesService.findByUserAndCategoryHundred(userEmail, category);
        return ResponseEntity.ok(clothesList);
    }

    // 사용자의 특정 카테고리 옷 목록 조회
    @GetMapping("/get/{userEmail}/{category}")
    public ResponseEntity<List<Clothes>> getClothesByCategory(@PathVariable String userEmail, @PathVariable Long category) {
        List<Clothes> clothesList = clothesService.findClothesByUserEmailAndCategory(userEmail, category);
        return ResponseEntity.ok(clothesList);
    }
}
