package teamProject_Server.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import teamProject_Server.Domain.Color;
import teamProject_Server.Domain.User;

@Repository
public interface ColorRepository extends JpaRepository<Color, Long> {
    Color findByUser(User user);
}
