package teamProject_Server.Service;

import org.springframework.stereotype.Service;

import java.security.SecureRandom;

@Service
public class UserPasswordGeneratorService {

    // 생성할 비밀번호의 길이
    private static final int PASSWORD_LENGTH = 10;

    // 랜덤 비밀번호 생성 메소드
    public static String generateRandomPassword() {
        // 가능한 문자 세트
        String CHAR_LOWER = "abcdefghijklmnopqrstuvwxyz";
        String CHAR_UPPER = CHAR_LOWER.toUpperCase();
        String NUMBER = "0123456789";
        String OTHER_CHAR = "!@#$%&*()_+-=[]?";

        // 모든 가능한 문자 세트
        String PASSWORD_ALLOW_BASE = CHAR_LOWER + CHAR_UPPER + NUMBER + OTHER_CHAR;
        SecureRandom random = new SecureRandom();

        StringBuilder password = new StringBuilder(PASSWORD_LENGTH);
        for (int i = 0; i < PASSWORD_LENGTH; i++) {
            // 비밀번호 문자 세트에서 랜덤한 문자 선택
            int randomIndex = random.nextInt(PASSWORD_ALLOW_BASE.length());
            password.append(PASSWORD_ALLOW_BASE.charAt(randomIndex));
        }
        return password.toString();
    }


}
