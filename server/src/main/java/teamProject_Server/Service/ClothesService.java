package teamProject_Server.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import teamProject_Server.Domain.Clothes;
import teamProject_Server.Domain.User;
import teamProject_Server.Repository.ClothesRepository;
import teamProject_Server.Repository.UserRepository;

import java.util.List;

@Service
public class ClothesService {
    private final ClothesRepository clothesRepository;
    private final UserRepository userRepository;

    @Autowired
    public ClothesService(ClothesRepository clothesRepository, UserRepository userRepository) {
        this.clothesRepository = clothesRepository;
        this.userRepository = userRepository;
    }

    public void saveClothes(Clothes clothes) {
        clothesRepository.save(clothes);
    }

    // 특정 사용자의 모든 옷 목록 조회
    public List<Clothes> findAllClothesByUserEmail(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new IllegalArgumentException("해당 이메일의 사용자를 찾을 수 없습니다: " + userEmail));
        return clothesRepository.findByUser(user);
    }

    // 백의 자리 카테고리 확인하여 일치하는 옷 목록 조회
    public List<Clothes> findByUserAndCategoryHundred(String userEmail, Long category) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new IllegalArgumentException("해당 이메일의 사용자를 찾을 수 없습니다: " + userEmail));
        return clothesRepository.findByUserAndCategoryHundred(user, category);
    }

    // 특정 사용자의 특정 카테고리 옷 목록 조회
    public List<Clothes> findClothesByUserEmailAndCategory(String userEmail, Long category) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new IllegalArgumentException("해당 이메일의 사용자를 찾을 수 없습니다: " + userEmail));
        return clothesRepository.findByUserAndClCategory(user, category);
    }
}
