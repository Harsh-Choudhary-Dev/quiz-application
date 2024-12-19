package com.neet_question_api.api.aop;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.neet_question_api.api.modal.ChapterIds;
import com.neet_question_api.api.repo.ApiLogRepository;
import com.neet_question_api.api.repo.ApiUsersRepo;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Data;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.*;
import java.util.stream.Collectors;

@Aspect
@Component
@Data
public class ApiCallsValidation {

    @Autowired
    private ApiLogRepository apiLogRepository;

    @Autowired
    private ObjectMapper objectMapper;

    String apiPlans = "{\n" +
            "    \"permittedQuestions\": {\n" +
            "        \"free\": 100,\n" +
            "        \"basic\": 1000,\n" +
            "        \"pro\": 5000,\n" +
            "        \"owner\": 10000000000000\n" +
            "    }\n" +
            "}";


    @Autowired
    private ApiUsersRepo apiUsersRepo;

    @Around("execution(* com.neet_question_api.api.controller.ApiController.*(..))")
    public Object validateApiCall(ProceedingJoinPoint joinPoint) throws Throwable {
//        System.out.println("before ");
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        String apiKey = request.getHeader("x-api-key");

        if (apiKey == null || apiKey.isEmpty()) {
            return objectMapper.createObjectNode().put("error", "API key missing or invalid");
        }

//        System.out.println("API Key: " + apiKey);
        JsonNode apiPlanJson = objectMapper.readTree(apiPlans);
//        System.out.println("API Plans: " + apiPlanJson);
        String userId = apiUsersRepo.findUserIdByApiKey(apiKey);
        System.out.println(apiKey);
        System.out.println(userId);
        if (userId == null) {
            return objectMapper.createObjectNode().put("error", "Invalid API key");
        }

        Integer questionCount = apiLogRepository.findTotalQuestionCountByUserId(userId);
//        System.out.println("question count: " + questionCount);

        String accountType = apiUsersRepo.findAccountTypeByUserId(userId);
        System.out.println(accountType);
        int apiPlanQuestions = apiPlanJson.get("permittedQuestions").get(accountType).asInt();
//        System.out.println("API Plan Questions: " + apiPlanQuestions);
        Object[] args = joinPoint.getArgs();
        if (args.length > 1 && args[1] instanceof String) {
            int argPassed;
            try {
                argPassed = Integer.parseInt((String) args[1]);
            } catch (NumberFormatException e) {
                return objectMapper.createObjectNode().put("error", "Invalid argument for number of questions");
            }
            if (questionCount != null && questionCount >= apiPlanQuestions) {
                return objectMapper.createObjectNode().put("error", "Resources exhausted");
            }
            if (questionCount != null && argPassed > (apiPlanQuestions - questionCount)) {
                int remainingQuestions = apiPlanQuestions - questionCount;
                return objectMapper.createObjectNode().put("error", "Limited to " + remainingQuestions + " more questions. Upgrade your plan.");
            }
        }
        return joinPoint.proceed();
    }


    @Around("execution(* com.neet_question_api.api.controller.ApiController.getCustomMixQuestions(..))")
    public Object checkQuestionLimitForJson(ProceedingJoinPoint joinPoint) throws Throwable {
//        System.out.println("------------------------------------------------------------------------------------");
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        String apiKey = request.getHeader("x-api-key");
//        System.out.println("API Key: " + apiKey);

// Check for missing or invalid API key
        if (apiKey == null || apiKey.isEmpty()) {
            return objectMapper.createObjectNode().put("error", "API key is missing");
        }

// Parse API plans and find userId
        JsonNode apiPlanJson = objectMapper.readTree(apiPlans);
        String userId = apiUsersRepo.findUserIdByApiKey(apiKey);

// Check if userId is valid
        if (userId == null) {
            return objectMapper.createObjectNode().put("error", "Invalid API key");
        }

        Integer questionCount = apiLogRepository.findTotalQuestionCountByUserId(userId);
        String accountType = apiUsersRepo.findAccountTypeByUserId(userId);

// Check if accountType is valid
        if (accountType == null) {
            return objectMapper.createObjectNode().put("error", "Account type not found for user");
        }

        int apiPlanQuestions = apiPlanJson.get("permittedQuestions").get(accountType).asInt();
        List<Integer> questionNumbers = new ArrayList<>();
//        System.out.println("Question count: " + questionCount);

// Get passed arguments and validate
        List<Object> args = List.of(joinPoint.getArgs());
        if (args.get(0) instanceof List<?> chapterIdsList && !chapterIdsList.isEmpty()) {
            for (ChapterIds chapterId : (List<ChapterIds>) chapterIdsList) {
                questionNumbers.add(Integer.valueOf(chapterId.getQuestion_number()));
            }
        }

        int questionSum = questionNumbers.stream().mapToInt(Integer::intValue).sum();
//        System.out.println("Total question sum: " + questionSum);

// Validate question limits
        if (questionCount != null) {
            if (questionCount.equals(apiPlanQuestions)) {
                return objectMapper.createObjectNode().put("error", "Resources Exhausted");
            }
            if (questionSum > (apiPlanQuestions - questionCount)) {
                return objectMapper.createObjectNode().put("error", "Resources limited to " + (apiPlanQuestions - questionCount) + ". Update your question limit");
            }
        }

        return joinPoint.proceed();


    }
}


