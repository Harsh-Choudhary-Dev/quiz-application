package com.neet_question_api.api.repo;

import com.fasterxml.jackson.databind.JsonNode;
import com.neet_question_api.api.modal.ApiLogs;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApiLogRepository extends JpaRepository<ApiLogs, Integer> {


    @Query(value = "SELECT SUM(a.question_count) FROM api_log a WHERE a.userid = :userId", nativeQuery = true)
    Integer findTotalQuestionCountByUserId(@Param("userId") String userId);

    // Native SQL Query to count records grouped by date for the last 7 days and a specific user
    @Query(value = "SELECT DATE(timestamp) AS log_date, COUNT(*) AS record_count " +
            "FROM api_log " +
            "WHERE timestamp >= NOW() - INTERVAL 7 DAY " +
            "AND userid = :userid " +
            "GROUP BY DATE(timestamp)", nativeQuery = true)
    List<Object[]> countRecordsByDateForLast7Days(@Param("userid") String userid);

    @Query(value = "SELECT al.method_name, COUNT(*) AS method_count " +
            "FROM api_log al " +
            "GROUP BY al.method_name " +
            "ORDER BY method_count DESC " +
            "LIMIT 1",
            nativeQuery = true)
    List<Object[]> findMostCalledMethod();
}
