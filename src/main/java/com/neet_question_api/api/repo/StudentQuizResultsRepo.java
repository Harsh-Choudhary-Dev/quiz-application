package com.neet_question_api.api.repo;

import com.neet_question_api.api.modal.StudentQuizResults;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository

public interface StudentQuizResultsRepo extends JpaRepository<StudentQuizResults, String> {
    @Query(value = "SELECT * FROM student_quiz_results WHERE quiz_id = :quizId", nativeQuery = true)
    List<StudentQuizResults> findByQuizId(@Param("quizId") String quizId);

    @Query(value = "SELECT * FROM student_quiz_results WHERE student_id = :studentId ORDER BY score DESC LIMIT 7;", nativeQuery = true)
    List<StudentQuizResults> findTop5ByStudentId(@Param("studentId") String studentId);

    @Query(value = """
        SELECT DATE(created_at) AS quiz_date, 
               SUM(score) AS total_score, 
               COUNT(*) AS total_quizzes, 
               AVG(score) AS average_score 
        FROM student_quiz_results 
        WHERE student_id = :studentId
        AND DATE(created_at) >= CURDATE() - INTERVAL :daysInterval DAY 
        
        GROUP BY DATE(created_at) 
        ORDER BY quiz_date DESC
        """, nativeQuery = true)
    List<Object[]> findQuizStatsForLastNDays(
            @Param("studentId") String studentId,
            @Param("daysInterval") int daysInterval);

    @Query(value = "SELECT " +
            "COUNT(quiz_id) AS totalQuizzes, " +
            "SUM(total_questions) AS totalQuestions, " +
            "AVG(score) AS averageScore, " +
            "AVG(time_taken) AS averageTimeTaken " +
            "FROM student_quiz_results " +
            "WHERE student_id = :studentId",
            nativeQuery = true)
    Map<String, Object> getStudentQuizStats(@Param("studentId") String studentId);

}
