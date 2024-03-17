package teamProject_Server.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import teamProject_Server.Domain.Style;
import teamProject_Server.Domain.User;

@Repository
public interface StyleRepository extends JpaRepository<Style, Long> {
    Style findByUser(User user);

}
