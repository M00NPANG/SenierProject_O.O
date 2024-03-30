package teamProject_Server.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import teamProject_Server.Domain.Clothes;
import teamProject_Server.Domain.Color;
import teamProject_Server.Domain.User;

import java.util.List;

@Repository
public interface ClothesRepository extends JpaRepository<Clothes, Long> {
    List<Clothes> findByUserId(Long userId);

    /* 오류나서 다 날려버림
    // 사용자별로 등록된 옷 목록 조회

    List<Clothes> findByUser(User user);

    // 사용자별로 특정 카테고리에 해당하는 옷 목록 조회
    List<Clothes> findByUserAndClCategory(User user, Long clCategory);

    @Query("SELECT c FROM Clothes c WHERE c.user_id = :user AND FUNCTION('FLOOR', c.cl_category / 100) = FUNCTION('FLOOR', :category / 100)")
    List<Clothes> findByUserAndCategoryHundred(User user, Long category);
    */
}
