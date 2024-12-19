package com.neet_question_api.api.repo;

import com.neet_question_api.api.modal.PerformanceMatrix;
import com.neet_question_api.api.modal.PerformanceSummary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PerformanceSummaryRepo extends JpaRepository<PerformanceSummary, String> {
    @Query(value = "SELECT cp.chapter_id AS chapter_id, " +
            "ch.ch_name AS chapter_name, " +
            "s.student_id AS student_id, " +
            "s.first_name AS first_name, " +
            "SUM(cp.score) AS total_score, " +
            "SUM(cp.total_questions) AS total_questions, " +
            "AVG(cp.score) AS avg_score, " +
            "AVG((cp.score / NULLIF(cp.total_questions, 0)) * 100) AS avg_accuracy " +
            "FROM chapter_performance cp " +
            "JOIN students s ON cp.student_id = s.student_id " +
            "JOIN chapter_id ch ON cp.chapter_id = ch.ch_id " +
            "WHERE cp.student_id = :studentId " +
            "AND DATE(cp.created_at) = CURRENT_DATE " +  // Fetch only today's data
            "GROUP BY cp.chapter_id, ch.ch_name, s.student_id, s.first_name " +
            "ORDER BY cp.chapter_id ASC", nativeQuery = true)
    List<PerformanceSummary> findChapterPerformanceByStudentAndInterval(
            @Param("studentId") String studentId
    );



}
