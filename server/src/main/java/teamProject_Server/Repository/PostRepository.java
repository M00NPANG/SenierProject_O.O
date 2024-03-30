package teamProject_Server.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import teamProject_Server.Domain.Color;
import teamProject_Server.Domain.Post;
import teamProject_Server.Domain.User;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByUserEmail(String userEmail);

}