package teamProject_Server.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import teamProject_Server.Domain.Color;
import teamProject_Server.Domain.Post;
import teamProject_Server.Domain.User;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByUserEmail(String userEmail);

    // JSON 필드를 처리하는 사용자 정의 메서드 또는 네이티브 쿼리
    @Query(value = "SELECT p FROM Post p WHERE p.userEmail <> :userEmail AND JSON_CONTAINS(p.hashtag, :hashtags, '$') AND p.user_percol = :personalColor", nativeQuery = true)
    List<Post> findRelevantPosts(String userEmail, String personalColor, String hashtags);

}