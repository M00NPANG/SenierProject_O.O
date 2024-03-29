package teamProject_Server.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import teamProject_Server.Domain.Color;
import teamProject_Server.Domain.Style;
import teamProject_Server.Domain.User;
import teamProject_Server.Repository.ColorRepository;
import teamProject_Server.Repository.StyleRepository;
import teamProject_Server.Repository.UserRepository;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final StyleRepository styleRepository;
    private final ColorRepository colorRepository;

    @Autowired
    public UserService(UserRepository userRepository, StyleRepository styleRepository, ColorRepository colorRepository)
    {
        this.userRepository = userRepository;
        this.colorRepository = colorRepository;
        this.styleRepository = styleRepository;
    }

    // 회원가입
    public String join(User user) {
        // 이메일(아이디) 중복 검사
        if (isEmailAlreadyExists(user.getEmail())) {
            throw new IllegalArgumentException("이미 사용 중인 이메일입니다. 다른 이메일을 입력해주세요.");
        }

        // 회원 정보를 데이터베이스에 저장하고 저장된 회원의 email 반환
        User savedUser = userRepository.save(user);

        // 사용자의 선호색 데이터 생성
        Color color = new Color(0, 0, 0, 0, 0, 0, 0, 0, savedUser);
        colorRepository.save(color);

        // 사용자의 선호스타일 데이터 생성
        Style style = new Style(savedUser, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
        styleRepository.save(style);

        return savedUser.getEmail();
    }

    // 이메일(아이디) 중복 확인
    public boolean isEmailAlreadyExists(String email) {
        Optional<User> existingUser = userRepository.findByEmail(email);
        return existingUser.isPresent(); // 이메일이 이미 존재하는지 여부를 반환
    }

    // 로그인
    public boolean login(String email, String password) {
        Optional<User> userOptional = userRepository.findByEmail(email);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            return user.getPassword().equals(password);
        }

        return false;
    }
}

