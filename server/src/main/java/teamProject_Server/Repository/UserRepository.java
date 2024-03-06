package teamProject_Server.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import teamProject_Server.Domain.User;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // user 저장
    User save(User user);

    // 특정 이메일 주소로 회원을 찾는 메소드
    Optional<User> findByEmail(String email);
}
