package teamProject_Server.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import teamProject_Server.Domain.UserPost;

import java.util.List;

@Repository
public interface UserPostRepository extends JpaRepository<UserPost, Long> {

    // 유저 ID에 따른 모든 게시물 찾기
    List<UserPost> findByUser_Email(String email);


}
