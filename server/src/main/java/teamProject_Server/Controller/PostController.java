package teamProject_Server.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import teamProject_Server.DTO.PostUpdateRequest;
import teamProject_Server.Domain.Post;
import teamProject_Server.Service.PostService;

import java.util.List;

@RestController
@RequestMapping("/api/storage")
public class PostController {
    private final PostService postService;
    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

    /**
     수정할 것
     - 게시물을 보관함에 저장하기 ( 보내온 배열을 각각 분류해서 게시물 저장)
     - 보관함에 저장된 추천 게시물 전송 (각 저장된 id를 통해 배열 형태로 전송)
     - 게시물에 저장된 아이템 클릴 시 아이템의 이미지 url를 통해 아이템 정보 반환하는 기능
     */


    // POST 저장 (입력 : 유저 이메일과 각 상의,하의,신발,악세서리,가방의 이미지 URL | 출력 : "성공적으로 post 저장됨)
    @PostMapping("/savePost")
    public ResponseEntity<String> savePost (@RequestBody PostUpdateRequest postUpdateRequest) {
        postService.savePost(postUpdateRequest);
        return ResponseEntity.ok("성공적으로 post 저장됨");
    }

    // 이메일에 해당하는 post 전송
    @GetMapping("/receivePost")
    public List<Post> getPostsByUser(@RequestParam String userEmail) {
        return postService.getPostsByUserEmail(userEmail);
    }

    @GetMapping("/receiveRecommendedPosts")
    public List<Post> getRecommendedPosts(@RequestParam String userEmail){
        return postService.getPostsNotByUserEmail(userEmail);
    }
}
