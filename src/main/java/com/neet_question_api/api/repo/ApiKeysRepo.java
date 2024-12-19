package com.neet_question_api.api.repo;

import com.neet_question_api.api.modal.ApiKeys;
import com.neet_question_api.api.modal.ApiLogs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface ApiKeysRepo extends JpaRepository<ApiKeys, Integer> {
    @Query(value = "SELECT created_at, api_name, user_id,api_id FROM api_keys WHERE user_id = ?1", nativeQuery = true)
    List<Map<String, Object>> findCreatedAtApiNameUserIdByUserId(String userId);
}
