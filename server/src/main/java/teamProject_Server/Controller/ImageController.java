package teamProject_Server.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import teamProject_Server.Domain.Post;
import teamProject_Server.Service.ImageService;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/image")
public class ImageController {
    private final ImageService imageService;

    @Autowired
    public ImageController(ImageService imageService) { this.imageService = imageService; }


    // 이미지 업로드
    @PostMapping("/upload")
    public ResponseEntity<String> uploadImage(@RequestParam("file") MultipartFile file,
                                              @RequestParam("category") String category,
                                              @RequestParam("userEmail") String userEmail) {
        try {
            String filePath = imageService.saveImage(userEmail, category, file);
            return ResponseEntity.ok("이미지 업로드 완료. 저장된 파일 경로: " + filePath);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
