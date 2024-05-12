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
    private UserService userService; // UserService 주입
    @Autowired
    public ClothesService(ClothesRepository clothesRepository, UserRepository userRepository) {
        this.clothesRepository = clothesRepository;
        this.userRepository = userRepository;
    }

    public void saveClothes(Clothes clothes) {
        clothesRepository.save(clothes);
    }

    // 이메일로 해당 유저의 옷아이템 전송
    public List<Clothes> getClothesByEmail(String email) {
        Long userId = userService.getUserIdByEmail(email); // 이메일로 user_id를 가져옴
        return clothesRepository.findByUserId(userId); // 해당 user_id를 가진 모든 Clothes를 반환
    }


}
