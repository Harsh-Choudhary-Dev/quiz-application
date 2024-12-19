package com.neet_question_api.api.repo;

import com.neet_question_api.api.modal.ApiUsers;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface ApiUsersRepo extends JpaRepository<ApiUsers, Integer> {
    @Query(value = "SELECT user_id FROM api_keys WHERE api_key = ?1", nativeQuery = true)
    String findUserIdByApiKey(String apiKey);
    @Query(value = "SELECT user_id,username,email,account_type FROM api_users WHERE email = :email", nativeQuery = true)
    List<Map<String, Object>> findUserIdByEmail(@Param("email") String email);


    @Modifying
    @Transactional
    @Query(value = "INSERT INTO api_keys (api_key, user_id, api_name,api_id) VALUES (:apiKey, :userId, :apiName,:apiId)", nativeQuery = true)
    void insertApiKey(@Param("apiKey") String apiKey, @Param("userId") String userId, @Param("apiName") String apiName,@Param("apiId") String api_id);


    @Modifying
    @Transactional
    @Query(value = "DELETE FROM api_keys WHERE user_id = :userId AND api_id = :apiId", nativeQuery = true)
    void deleteByUserIdAndApiId(@Param("userId") String userId, @Param("apiId") String apiId);

    @Query(value = "SELECT account_type FROM api_users WHERE user_id = :userId", nativeQuery = true)
    String findAccountTypeByUserId(@Param("userId") String userId);
}
