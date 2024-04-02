package teamProject_Server.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import teamProject_Server.Domain.Preferences;
import teamProject_Server.Domain.User;

import java.util.Optional;

@Repository
public interface PreferencesRepository extends JpaRepository<Preferences, Long> {
    Optional<Preferences> findByUser(User user);
}
