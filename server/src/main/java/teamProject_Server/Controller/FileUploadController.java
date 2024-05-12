package teamProject_Server.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import teamProject_Server.Domain.Clothes;
import teamProject_Server.Domain.Post;
import teamProject_Server.Service.ClothesService;
import teamProject_Server.Repository.FileStorageService;
import teamProject_Server.Service.PostService;
import teamProject_Server.Service.UserService;

import java.io.IOException;

@RestController
public class FileUploadController {
    @Autowired
    private UserService userService;
    @Autowired
    private PostService postService;
    @Autowired
    private ClothesService clothesService;
    @Autowired
    private FileStorageService fileStorageService;

    @PostMapping("/upload") // 단순히 이미지 보내는 것을 확인한 용도임
    public String uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            return fileStorageService.uploadFile(file);
        } catch (IOException e) {
            // 적절한 예외 처리
            return "File upload failed: " + e.getMessage();
        }
    }

    @PostMapping("/uploadCodi")
    public ResponseEntity<String> uploadCodi(
            @RequestPart("image") MultipartFile image,
            @RequestParam("title") String title,
            @RequestParam("hashtag") String hashtag,
            @RequestParam("comment") String comment,
            @RequestParam("useremail") String userEmail) {

        if (image.isEmpty()) {
            return ResponseEntity.badRequest().body("The image file is empty and was not uploaded.");
        }

        try {
            // 파일을 업로드하고, 업로드된 파일의 URL을 반환받음
            String fileUrl = fileStorageService.uploadFile(image);

            String userName = userService.findUsernameByEmail(userEmail);
            if (userName == null) {
                return ResponseEntity.badRequest().body("User email not found.");
            }

            Post post = new Post(hashtag, comment, title, fileUrl, userEmail, userName);
            postService.savePost(post);

            return ResponseEntity.ok("Codi uploaded successfully and saved to database.");
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("File upload failed: " + e.getMessage());
        }
    }

    @PostMapping("/uploadClothes")
    public ResponseEntity<String> uploadClothes(
            @RequestPart("image") MultipartFile image,
            @RequestParam("cl_category") String cl_category,
            @RequestParam("useremail") String userEmail) {

        if (image.isEmpty()) {
            return ResponseEntity.badRequest().body("The image file is empty and was not uploaded.");
        }

        try {
            // 파일을 업로드하고, 업로드된 파일의 URL을 반환받음
            String fileUrl = fileStorageService.uploadFile(image);
            long clCategory = Integer.parseInt(cl_category);
            long userId = userService.getUserIdByEmail(userEmail);

            if (userId == 0) {
                return ResponseEntity.badRequest().body("User email not found.");
            }
            Clothes clothes = new Clothes();
            clothes.setCl_category(clCategory); // cl_category에 값 설정
            clothes.setUserId(userId); // user_id에 User 인스턴스 설정
            clothes.setCl_photo_path(fileUrl); // cl_photo_path에 값 설정
            clothesService.saveClothes(clothes);

            return ResponseEntity.ok("Clothes uploaded successfully and saved to database.");
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("File upload failed: " + e.getMessage());
        }
    }
}

