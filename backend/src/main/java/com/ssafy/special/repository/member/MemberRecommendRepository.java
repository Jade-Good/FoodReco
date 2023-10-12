package com.ssafy.special.repository.member;

import com.ssafy.special.domain.member.Member;
import com.ssafy.special.domain.member.MemberRecommend;
import com.ssafy.special.dto.RecentRecommendFoodDto;
import com.ssafy.special.dto.RecentRecommendFoodResult;
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
    @Query(value = "SELECT f.food_seq as foodSeq, f.name FROM member_recommend mr join food f on mr.food_seq = f.food_seq WHERE mr.member_seq = :memberSeq AND DATEDIFF(:now, mr.recommend_at) < 14 and DATEDIFF(:now, mr.recommend_at) > 7 AND mr.food_rating > 1 ORDER BY mr.food_rating DESC, mr.recommend_at DESC", nativeQuery = true)
    List<RecentRecommendFoodResult>
    findRecentlyRecommendedFood(@Param("memberSeq") Long memberSeq, @Param("now")LocalDateTime now);

    List<MemberRecommend> findAllByMemberOrderByRecommendAtDesc(Member member);
//    테스트용
//    @Query("SELECT mr.food.foodSeq, mr.food.name FROM member_recommend mr WHERE mr.member.memberSeq = :memberSeq AND DATEDIFF(:now, mr.recommendAt) < 7 and DATEDIFF(:now, mr.recommendAt) > 7 AND mr.foodRating > 0 ORDER BY mr.foodRating DESC, mr.recommendAt DESC")
//    List<RecentRecommendFoodDto>
//    findRecentlyRecommendedFood(@Param("memberSeq") Long memberSeq, @Param("now")LocalDateTime now);



    @Query(value = "SELECT mr.food_seq FROM member_recommend mr where mr.member_seq = :memberSeq AND DATEDIFF(:now, mr.recommend_at) < 8 AND TIMESTAMPDIFF(MINUTE, mr.recommend_at, :now) >= 0", nativeQuery = true)
    List<Long>
    findMemberRecommendsWithinOneWeek(@Param("memberSeq") Long memberSeq, @Param("now") LocalDateTime now);


    @Query(value = "SELECT mr.food_seq as foodSeq, f.name FROM member_recommend mr join food f on mr.food_seq = f.food_seq WHERE mr.member_seq = :member_seq AND mr.weather = :weather AND mr.activity_calorie BETWEEN :active_calorie1 and :active_calorie2 AND mr.food_rating > 2", nativeQuery = true)
    List<RecentRecommendFoodResult>
    findSimilarRecommendedFood(@Param("member_seq") Long member_seq, @Param("weather") String weather, @Param("active_calorie1") int active_calorie1, @Param("active_calorie2") int active_calorie2);


}
