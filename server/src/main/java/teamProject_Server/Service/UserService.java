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

        // 닉네임 중복 검사
        if(isNameAlreadyExists(user.getName())) {
            throw new IllegalArgumentException("이미 사용중인 닉네임입니다. 다른 닉네임을 입력해주세요.");
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

    // 이메일(아이디) 중복 확인하는 함수
    public boolean isEmailAlreadyExists(String email) {
        Optional<User> existingUser = userRepository.findByEmail(email);
        return existingUser.isPresent(); // 이메일이 이미 존재하는지 여부를 반환
    }

    // 닉네임 중복 확인하는 함수
    public boolean isNameAlreadyExists(String name) {
        Optional<User> existingUserName = userRepository.findByName(name);
        return existingUserName.isPresent();
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

   /* // 이메일, 닉네임을 확인하고 랜덤 비밀번호를 생성하여 저장하기
    public String findPassword(String email, String name) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (user.getName().equals(name)) {
                String newPassword = generateRandomPassword();
                user.setPassword(newPassword);
                userRepository.save(user);
                return newPassword;
            } else {
                throw new IllegalArgumentException("이메일과 이름이 일치하지 않습니다.");
            }
        } else {
            throw new IllegalArgumentException("해당 이메일로 가입된 사용자가 없습니다.");
        }
    }*/

    // 비밀번호 변경(1) - 유저 확인
    public User verifyUser(String email, String name) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (user.getName().equals(name)) {
                return user;
            } else {
                throw new IllegalArgumentException("이메일과 이름이 일치하지 않습니다.");
            }
        } else {
            throw new IllegalArgumentException("해당 이메일로 가입된 사용자가 없습니다.");
        }
    }

    // 비밀번호 변경(1) - 비밀번호 변경
    public void editPassword(User user, String newUserPassword) {
        user.setPassword(newUserPassword);
        userRepository.save(user);
    }

    public String findUsernameByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(User::getName) // 사용자의 이름을 가져옵니다.
                .orElse(null); // 이메일에 해당하는 사용자가 없다면 null을 반환합니다.
    }

    public Long getUserIdByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(User::getUser_id) // Optional<User>를 Optional<Long>로 변환
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
    }



}

