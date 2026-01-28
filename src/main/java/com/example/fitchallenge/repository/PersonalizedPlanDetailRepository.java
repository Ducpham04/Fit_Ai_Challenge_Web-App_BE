package com.example.fitchallenge.repository;

import com.example.fitchallenge.entity.PersonalizedPlanDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PersonalizedPlanDetailRepository extends JpaRepository<PersonalizedPlanDetail, Long> {
    
    /**
     * Tìm tất cả bài tập cá nhân hóa của user
     */
    List<PersonalizedPlanDetail> findByUser_Id(Long userId);
    
    /**
     * Tìm bài tập cá nhân hóa theo user và day number
     */
    List<PersonalizedPlanDetail> findByUser_IdAndDayNumber(Long userId, Integer dayNumber);
    
    /**
     * Tìm bài tập cá nhân hóa cho hôm nay (theo day number)
     */
    @Query("SELECT ppd FROM PersonalizedPlanDetail ppd " +
           "WHERE ppd.user.id = :userId AND ppd.dayNumber = :dayNumber " +
           "ORDER BY ppd.id ASC")
    List<PersonalizedPlanDetail> findByUserIdAndDayNumber(
        @Param("userId") Long userId, 
        @Param("dayNumber") Integer dayNumber
    );
    
    /**
     * Tìm bài tập cá nhân hóa theo user, day number và challenge
     */
    Optional<PersonalizedPlanDetail> findByUser_IdAndDayNumberAndChallenge_Id(
        Long userId, 
        Integer dayNumber, 
        Long challengeId
    );
}


