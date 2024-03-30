package teamProject_Server.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import teamProject_Server.Domain.Post;
import teamProject_Server.Repository.PostRepository;

import java.util.List;

@Service
public class PostService {

    private final PostRepository postRepository;

    @Autowired
    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    // 게시물 저장
    public Post savePost(Post post) {
        return postRepository.save(post);
    }

    // 해당 유저의 게시물 전송
    public List<Post> getPostsByUserEmail(String userEmail) {
        return postRepository.findByUserEmail(userEmail);
    }
}
