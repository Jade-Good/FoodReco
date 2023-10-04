package com.ssafy.special.repository.member;

import com.ssafy.special.domain.member.Member;
import com.ssafy.special.domain.member.MemberRecommend;
import com.ssafy.special.dto.RecentRecommendFoodDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface MemberRecommendRepository extends JpaRepository<MemberRecommend, Long>{
    Optional<MemberRecommend> findMemberRecommendByMember_MemberSeqAndFood_FoodSeq( Long memberSeq, Long foodSeq);

    @Query("SELECT mr FROM member_recommend mr WHERE mr.member.memberSeq = :memberSeq AND mr.food.foodSeq = :foodSeq ORDER BY mr.recommendAt DESC")
    List<MemberRecommend> findLatestMemberRecommend(@Param("memberSeq") Long memberSeq, @Param("foodSeq") Long foodSeq, Pageable pageable);

    @Query("UPDATE member_recommend mr SET mr.foodRating = :newRate WHERE mr.memberRecommendSeq = :memberRecommendSeq")
    @Modifying
    void updateFoodRating(@Param("newRate") int newRate, @Param("memberRecommendSeq") Long memberRecommendSeq);

    /* 14일 이전부터 7일 전까지 추천받은 음식 리스트 */
    @Query("SELECT mr.food.foodSeq, mr.food.name FROM member_recommend mr WHERE mr.member.memberSeq = :memberSeq AND DATEDIFF(:now, mr.recommendAt) < 14 and DATEDIFF(:now, mr.recommendAt) > 7 AND mr.foodRating > 1 ORDER BY mr.foodRating DESC, mr.recommendAt DESC")
    List<RecentRecommendFoodDto>
    findRecentlyRecommendedFood(@Param("memberSeq") Long memberSeq, @Param("now")LocalDateTime now);

    List<MemberRecommend> findAllByMemberOrderByRecommendAtDesc(Member member);
//    테스트용
//    @Query("SELECT mr.food.foodSeq, mr.food.name FROM member_recommend mr WHERE mr.member.memberSeq = :memberSeq AND DATEDIFF(:now, mr.recommendAt) < 7 and DATEDIFF(:now, mr.recommendAt) > 7 AND mr.foodRating > 0 ORDER BY mr.foodRating DESC, mr.recommendAt DESC")
//    List<RecentRecommendFoodDto>
//    findRecentlyRecommendedFood(@Param("memberSeq") Long memberSeq, @Param("now")LocalDateTime now);



    @Query(value = "SELECT mr.food_seq FROM member_recommend mr where mr.member_seq = :memberSeq AND DATEDIFF(:now, mr.recommend_at) < 8 AND TIMESTAMPDIFF(MINUTE, mr.recommend_at, :now) >= 0", nativeQuery = true)
    List<Long>
    findMemberRecommendsWithinOneWeek(@Param("memberSeq") Long memberSeq, @Param("now") LocalDateTime now);

    @Query("SELECT mr.food.foodSeq, mr.food.name FROM member_recommend mr WHERE mr.member.memberSeq = :memberSeq AND mr.weather = :weather AND mr.activityCalorie BETWEEN :activeCalorie-1000 and :activeCalorie+1000")
    List<RecentRecommendFoodDto>
    findSimilarRecommendedFood1000(@Param("memberSeq") Long memberSeq, @Param("weather") String weather, @Param("activeCalorie") int activeCalorie);

    @Query("SELECT mr.food.foodSeq, mr.food.name FROM member_recommend mr WHERE mr.member.memberSeq = :memberSeq AND mr.weather = :weather AND mr.activityCalorie BETWEEN :activeCalorie-2000 and :activeCalorie+2000")
    List<RecentRecommendFoodDto>
    findSimilarRecommendedFood2000(@Param("memberSeq") Long memberSeq, @Param("weather") String weather, @Param("activeCalorie") int activeCalorie);

    @Query("SELECT mr.food.foodSeq, mr.food.name FROM member_recommend mr WHERE mr.member.memberSeq = :memberSeq AND mr.weather = :weather AND mr.activityCalorie BETWEEN :activeCalorie-3000 and :activeCalorie+3000")
    List<RecentRecommendFoodDto>
    findSimilarRecommendedFood3000(@Param("memberSeq") Long memberSeq, @Param("weather") String weather, @Param("activeCalorie") int activeCalorie);

}
