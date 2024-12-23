package com.neet_question_api.api.service;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.neet_question_api.api.modal.ApiResponse;
import com.neet_question_api.api.modal.ApiUsers;
import com.neet_question_api.api.modal.Questions;
import com.neet_question_api.api.modal.Students;
import com.neet_question_api.api.repo.*;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Timestamp;
import java.util.*;

@Data
@Service
public class UserService {
    @Autowired
    private ApiUsersRepo apiUsersRepo;
    @Autowired
    private UniqueIds uniqueIds;
    @Autowired
    private FeedbackRepo feedbackRepo;
    @Autowired
    private final ObjectMapper objectMapper;

    @Autowired
    private ApiKeysRepo apiKeysRepo;
    @Autowired
    private ApiLogRepository apiLogRepository;

    @Autowired
    private StudentsRepo studentsRepo;

    public Object createNewAccount(String email, String username) {
        List userIdByEmail = apiUsersRepo.findUserIdByEmail(email);
        List<ApiUsers> apiUsers = null;
        if (userIdByEmail.isEmpty()) {
            apiUsers = new ArrayList<>();
            String user_id = uniqueIds.generateUserId();
            System.out.println(user_id);
            Timestamp timeNow = new Timestamp(System.currentTimeMillis());
            apiUsers.add(new ApiUsers(user_id, email, timeNow, username));
            apiUsersRepo.save(apiUsers.get(0));
            return apiUsers.get(0);
        }else {
            return "email already exists";
        }
    }

public List<Map<String, Object>> fetchUserId(String email){
    return apiUsersRepo.findUserIdByEmail(email);

}
public ResponseEntity createApikey(String userId, String api_name) throws SQLIntegrityConstraintViolationException {
    System.out.println(userId);
    String api_key = "neet_" + uniqueIds.generateApiKey();
    String api_id = uniqueIds.generateUserId();
    ApiResponse response;
    try {
        apiUsersRepo.insertApiKey(api_key, userId, api_name, api_id);
        response = new ApiResponse("API key added successfully.", api_key, api_id);
    } catch (Exception e) {
        // Catch all other exceptions
        System.out.println("Error: " + e.getMessage());  // Print only the error message
        response = new ApiResponse(e.getMessage(), "error occurred", "Error: " + e.getMessage());
    }
    return ResponseEntity.ok(response);
}

public List deleteApiKey(String userId,String api_id){
    apiUsersRepo.deleteByUserIdAndApiId(userId,api_id);
    return Collections.singletonList("API key deleted successfully.");
}

public List<Map<String, Object>> fetchApiKeysData(String userid){
    return apiKeysRepo.findCreatedAtApiNameUserIdByUserId(userid);
}

public List fetchApiUsage(String userid){
    ObjectNode wrappedJson = objectMapper.createObjectNode();
    Integer question_count = apiLogRepository.findTotalQuestionCountByUserId(userid);
    List<Object[]> logs = apiLogRepository.countRecordsByDateForLast7Days(userid);
    if (question_count == null || logs == null) {
        wrappedJson.put("error", (question_count == null ? "Question count is null. " : "")
                + (logs == null ? "Logs data is null." : ""));
        return Collections.singletonList(wrappedJson);
    }
    wrappedJson.set("data", objectMapper.valueToTree(logs));
    wrappedJson.put("question_count", question_count);
    return Collections.singletonList(wrappedJson);
}
    public String fetchAccountType(String userid){
        return apiUsersRepo.findAccountTypeByUserId(userid);
    }

    public ObjectNode fetchStudentAnalyticts(){
        ObjectNode wrappedJson = objectMapper.createObjectNode();
        long question_count = apiLogRepository.findTotalQuestionCountByUserId("owner");
        List<Object[]> result = apiLogRepository.findMostCalledMethod();
        Object[] topResult = result.get(0);
        System.out.println(topResult[0]);
        System.out.println(topResult[1]);
        long user_count = studentsRepo.count();
        long feedback_count = feedbackRepo.count();
        wrappedJson.put("question_count",question_count);
        wrappedJson.put("user_count",user_count);
        wrappedJson.put("feedback_count",feedback_count);
        wrappedJson.put("method_name", topResult[0].toString());
        wrappedJson.put("method_count", topResult[1].toString());
        return wrappedJson;
    }

    public List<Students> fetchStudents(){
        return studentsRepo.findAll();
    }


    }



