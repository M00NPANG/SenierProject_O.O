package teamProject_Server.Controller;

import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import teamProject_Server.Domain.Post;
import teamProject_Server.Service.PostService;
import teamProject_Server.Service.UserService;
import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@RestController
public class testController {
    @Value("${upload.directory}")
    private String uploadDirectory;
    @Autowired
    private PostService postService;
    @Autowired
    private UserService userService;
    @Value("${gcp.storage.bucket.name}")
    private String bucketName;


    @PostMapping("/testUpload")
    public String handleFileUpload(@RequestParam("file") MultipartFile file) {

        try {
            file.transferTo(new File(uploadDirectory + file.getOriginalFilename()));
            return "File uploaded successfully: " + file.getOriginalFilename();
        } catch (Exception e) {
            return "File upload failed: " + e.getMessage();
        }
    }
        @PostMapping("/testCodiUpload")
        public ResponseEntity<String> uploadCodiSet(
                @RequestPart("image") MultipartFile image,
                @RequestParam("title") String title,
                @RequestParam("hashtag") String hashtag,
                @RequestParam("comment") String comment,
                @RequestParam("useremail") String userEmail) {

            try {

                // 사용자 이메일에 해당하는 디렉토리 경로 생성
                String userDirectoryPath = "src/main/resources/static/files/images/" + userEmail;
                Path userDirectory = Paths.get(userDirectoryPath);

                // 사용자 이메일의 디렉토리가 존재하지 않으면 생성
                if (!Files.exists(userDirectory)) {
                    Files.createDirectories(userDirectory);
                }

                // UUID를 사용하여 고유한 파일 이름 생성
                String fileName = UUID.randomUUID().toString() + "_" + image.getOriginalFilename();
                Path filePath = userDirectory.resolve(fileName);
                Files.write(filePath, image.getBytes());

                // UserService를 사용하여 useremail로 username을 조회
                String userName = userService.findUsernameByEmail(userEmail);
                if (userName == null) {
                    return ResponseEntity.badRequest().body("User email not found.");
                }
                String relativePath = "/files/images/" + userEmail + "/" + fileName;
                String webPath = relativePath.replace("\\", "/");
                // Post 엔티티 생성 및 저장
                Post post = new Post(hashtag, comment,title, relativePath, userEmail,userName);
                postService.savePost(post);

            } catch (IOException e) {
                e.printStackTrace();
                return ResponseEntity.internalServerError().body("Failed to save uploaded file and data.");
            }

            // 성공 응답 반환
            return ResponseEntity.ok("CodiSet uploaded and saved successfully.");
        }


}
