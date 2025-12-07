package com.example.fitchallenge.repository;

import com.example.fitchallenge.Entity.PersonalizedPlanDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PersonalizedPlanDetailRepository extends JpaRepository<PersonalizedPlanDetail, Long> {
    
    List<PersonalizedPlanDetail> findByUserTraining_UtId(Long utId);
    
    @Query("SELECT ppd FROM PersonalizedPlanDetail ppd " +
           "WHERE ppd.userTraining.utId = :utId AND ppd.trainingPlanDetail.dayNumber = :dayNumber")
    List<PersonalizedPlanDetail> findByUserTrainingAndDayNumber(
        @Param("utId") Long utId, 
        @Param("dayNumber") Integer dayNumber
    );
    
    Optional<PersonalizedPlanDetail> findByUserTraining_UtIdAndTrainingPlanDetail_TpdId(
        Long utId, 
        Long tpdId
    );
}

