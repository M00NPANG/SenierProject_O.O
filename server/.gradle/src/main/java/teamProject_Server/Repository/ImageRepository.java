package teamProject_Server.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import teamProject_Server.Domain.Image;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {

}
