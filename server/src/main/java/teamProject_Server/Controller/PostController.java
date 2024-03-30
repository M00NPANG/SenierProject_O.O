package teamProject_Server.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import teamProject_Server.Domain.Post;
import teamProject_Server.Repository.PostRepository;
import teamProject_Server.Service.PostService;

import java.util.List;

@RestController
public class PostController {
    private final PostService postService;
    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping("/receivePost")
    public List<Post> getPostsByUser(@RequestParam String userEmail) {
        return postService.getPostsByUserEmail(userEmail);
    }

}
