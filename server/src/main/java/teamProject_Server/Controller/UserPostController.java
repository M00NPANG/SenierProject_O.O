package teamProject_Server.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import teamProject_Server.Domain.UserPost;
import teamProject_Server.Service.UserPostService;

import java.io.IOException;
import java.util.List;

@RequestMapping("/api/userPost")
@RestController
public class UserPostController {

    private final UserPostService userPostService;

    @Autowired
    public UserPostController(UserPostService userPostService) {
        this.userPostService = userPostService;
    }


    // 게시물 작성 요청 처리
    @PostMapping("/upload/{userEmail}")
    public ResponseEntity<Long> createPost(@PathVariable String userEmail,
                                           @RequestParam String postTitle,
                                           @RequestParam String postContent,
                                           @RequestParam Long postLikes,
                                           @RequestParam String postColor,
                                           @RequestParam("postImage") MultipartFile postImage) {
        ResponseEntity<Long> responseEntity = userPostService.createPost(userEmail, postTitle, postContent, postLikes, postColor, postImage);
        return responseEntity;
    }

    // 특정 게시물 ID로 게시물 데이터와 이미지 바이트 데이터 반환
    @GetMapping("/getPost/{postId}")
    public ResponseEntity<UserPost> getPostById(@PathVariable Long postId) {
        try {
            UserPost post = userPostService.getPostById(postId);
            return ResponseEntity.ok(post);
        } catch (IOException | IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    // 특정 사용자의 모든 게시물과 이미지 바이트 데이터 반환
    @GetMapping("/getAllPostsByUser/{userEmail}")
    public ResponseEntity<List<UserPost>> getAllPostsByUser(@PathVariable String userEmail) {
        try {
            List<UserPost> posts = userPostService.getAllPostsByUser(userEmail);
            return ResponseEntity.ok(posts);
        } catch (IOException e) {
            return ResponseEntity.internalServerError().body(null);
        }
    }
}
