package teamProject_Server.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import teamProject_Server.DTO.PostUpdateRequest;
import teamProject_Server.Domain.Post;
import teamProject_Server.Repository.ClothesRepository;
import teamProject_Server.Repository.PostRepository;
import teamProject_Server.Repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostService {
    private final PostRepository postRepository;
    private final ClothesRepository clothesRepository;
    private final UserRepository userRepository;

    @Autowired
    public PostService(PostRepository postRepository, UserRepository userRepository, ClothesRepository clothesRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.clothesRepository = clothesRepository;
    }

    // post 저장
    public void savePost(PostUpdateRequest request) {
        Post post = new Post();
        post.setUserEmail(request.getEmail());
        post.setTop_id(clothesRepository. findIdByPhotoPath(request.getTopUrl()));
        post.setBottom_id(clothesRepository.findIdByPhotoPath(request.getBottomUrl()));
        post.setShoes_id(clothesRepository.findIdByPhotoPath(request.getShoesUrl()));
        post.setAccessories_id(clothesRepository.findIdByPhotoPath(request.getAccessoriesUrl()));
        post.setBag_id(clothesRepository.findIdByPhotoPath(request.getBagUrl()));

        postRepository.save(post);
    }

    // 유저 이메일이 저장된 포스트 클라이언트로 전송
    public List<Post> getPostsByUserEmail(String userEmail) {

        return postRepository.findByUserEmail(userEmail);
    }

    // 모든 포스트를 조회하되, 주어진 userEmail을 제외하고 최대 20개까지만 반환
    public List<Post> getPostsNotByUserEmail(String userEmail) {
        return postRepository.findAll().stream()
                .filter(post -> !post.getUserEmail().equals(userEmail))
                .limit(20)
                .collect(Collectors.toList());
    }

}
