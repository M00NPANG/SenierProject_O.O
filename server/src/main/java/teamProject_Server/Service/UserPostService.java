package teamProject_Server.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import teamProject_Server.Domain.User;
import teamProject_Server.Domain.UserPost;
import teamProject_Server.Repository.UserPostRepository;
import teamProject_Server.Repository.UserRepository;

import java.io.IOException;
import java.util.List;

@Service
public class UserPostService {

    private final UserPostRepository userPostRepository;
    private final UserRepository userRepository;
    private final ImageService imageService;

    @Autowired
    public UserPostService(UserPostRepository userPostRepository, UserRepository userRepository, ImageService imageService) {
        this.userPostRepository = userPostRepository;
        this.userRepository = userRepository;
        this.imageService = imageService;
    }


    // 게시물 작성 기능
    public ResponseEntity<Long> createPost(String userEmail, String postTitle, String postContent, Long postLikes, String postColor, MultipartFile postImage) {
        User user = userRepository.findByEmail(userEmail).orElseThrow(() -> new IllegalArgumentException("해당 이메일의 사용자가 없습니다."));

        // 이미지 저장 후 URL 받기
        String imageUrl = imageService.saveImage(userEmail, "posts", postImage);

        // 게시물 객체 생성
        UserPost post = new UserPost(user, postTitle, postContent, null, null, null, postLikes, postColor, imageUrl, user.getUser_img());

        // DB에 게시물 저장
        UserPost savedPost = userPostRepository.save(post);

        // 저장된 게시물의 ID 반환
        return new ResponseEntity<>(savedPost.getPost_id(), HttpStatus.CREATED);
    }

    // 게시물 ID로 게시물 찾기
    public UserPost getPostById(Long postId) throws IOException {
        UserPost post = userPostRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시물을 찾을 수 없습니다: " + postId));

        if (post.getPost_img() != null) {
            byte[] postImageBytes = imageService.loadImageAsBytes(post.getPost_img());
            post.setPostImageBytes(postImageBytes);
        }

        if (post.getUser_id() != null && post.getUser_id().getUser_img() != null) {
            byte[] userImageBytes = imageService.loadImageAsBytes(post.getUser_id().getUser_img());
            post.setUserImageBytes(userImageBytes); // UserPost 엔티티에 사용자 이미지 바이트 데이터 설정
        }

        return post;
    }

    // 사용자 이메일로 모든 관련 게시물 찾기
    public List<UserPost> getAllPostsByUser(String userEmail) throws IOException {
        List<UserPost> posts = userPostRepository.findByUser_Email(userEmail);

        for (UserPost post : posts) {
            // 게시물 이미지 바이트 데이터 처리
            if (post.getPost_img() != null) {
                byte[] postImageBytes = imageService.loadImageAsBytes(post.getPost_img());
                post.setPostImageBytes(postImageBytes);
            }

            // 사용자 이미지 바이트 데이터 처리
            if (post.getUser_id() != null && post.getUser_id().getUser_img() != null) {
                byte[] userImageBytes = imageService.loadImageAsBytes(post.getUser_id().getUser_img());
                post.setUserImageBytes(userImageBytes);
            }
        }

        return posts;
    }
}


