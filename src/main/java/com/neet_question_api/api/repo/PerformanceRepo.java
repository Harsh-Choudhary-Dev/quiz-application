package com.neet_question_api.api.repo;

import com.neet_question_api.api.modal.ApiKeys;
import com.neet_question_api.api.modal.PerformanceMatrix;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PerformanceRepo extends JpaRepository<PerformanceMatrix, Integer> {
    @Modifying
    @Query(value = "INSERT INTO chapter_performance (student_id, chapter_id, score, created_at, total_questions) " +
            "VALUES (:#{#performanceMatrix.student_id}, :#{#performanceMatrix.chapter_id}, :#{#performanceMatrix.score}, :#{T(java.time.LocalDateTime).now()}, :#{#performanceMatrix.total_questions})",
            nativeQuery = true)
    void insertChapterPerformance(@Param("performanceMatrix") PerformanceMatrix performanceMatrix);

}

