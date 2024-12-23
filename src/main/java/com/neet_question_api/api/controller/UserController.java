package com.neet_question_api.api.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.neet_question_api.api.modal.ChapterIds;
import com.neet_question_api.api.modal.Students;
import com.neet_question_api.api.service.QuestionsService;
import com.neet_question_api.api.service.UserService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Data
@RestController

@RequestMapping("api/owner/v1")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private QuestionsService questionsService;
    @PostMapping("/create-account")
    public List createUserAccount(@RequestParam String email,@RequestParam String username) {
        return Collections.singletonList(userService.createNewAccount(email, username));
    }
    @GetMapping("/login")
    public List<Map<String, Object>> getLoginPermissions(@RequestParam String email) {
        return userService.fetchUserId(email);
    }
    @PostMapping("/create-api-key")
    public List createNewApiKey(@RequestParam String userId,@RequestParam String api_name) throws SQLIntegrityConstraintViolationException {
        return Collections.singletonList(userService.createApikey(userId,api_name));
    }
    @PostMapping("/delete-api-key")
    public List deleteApiKey(@RequestParam String userId,@RequestParam String api_id) {
        return Collections.singletonList(userService.deleteApiKey(userId,api_id));
    }
    @GetMapping("/api-logs")
    public List getApiLogs(@RequestParam String userId) {
        return userService.fetchApiUsage(userId);
    }
    @GetMapping("/api-keys")
    public List getApiKeys(@RequestParam String userId) {
        return userService.fetchApiKeysData(userId);
    }
    @GetMapping("/account-type")
    public List getAccountType(@RequestParam String userId) {
        return Collections.singletonList(userService.fetchAccountType(userId));
    }
    @GetMapping("/students-analytics")
    public ObjectNode getStudentAnalytics(){
        return userService.fetchStudentAnalyticts();
    }

    @GetMapping("/students")
    public List<Students> getStudents(){
        return userService.fetchStudents();
    }

    @GetMapping("/all-questions")
    public JsonNode getAllQuestions(@RequestParam List<ChapterIds> chapter_id){
            return questionsService.fetchCustomMixQuestions(chapter_id);
        }
    }


