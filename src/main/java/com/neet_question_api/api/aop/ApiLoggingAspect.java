package com.neet_question_api.api.aop;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.neet_question_api.api.modal.ApiLogs;
import com.neet_question_api.api.repo.ApiLogRepository;
import com.neet_question_api.api.repo.ApiUsersRepo;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Data;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

@Aspect
@Component
@Data
public class ApiLoggingAspect {

    String apiPlans = "{\n" +
            "    \"permittedQuestions\": {\n" +
            "        \"free\": 100,\n" +
            "        \"basic\": 1000,\n" +
            "        \"pro\": 5000,\n" +
            "        \"owner\": 200000000000\n" +
            "    }\n" +
            "}";

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private ApiUsersRepo apiUsersRepo;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ApiLogRepository apiLogRepository;

    private static final Logger logger = LoggerFactory.getLogger(ApiLoggingAspect.class);

    @AfterReturning(pointcut = "execution(* com.neet_question_api.api.controller.ApiController.*(..))&& " +
            "!execution(* com.neet_question_api.api.controller.ApiController.getCustomMixQuestions(..)) && " +
            "!execution(* com.neet_question_api.api.controller.ApiController.getExamModeQuestions(..)) && " +
            "!execution(* com.neet_question_api.api.controller.ApiController.getPerformanceBasedMock(..)) && " +
            "!execution(* com.neet_question_api.api.controller.ApiController.getStudentResult(..)) && " +
            "!execution(* com.neet_question_api.api.controller.ApiController.wrongOrRight(..)) && " +
            "!execution(* com.neet_question_api.api.controller.ApiController.getStudentStats(..)) && " +
            "!execution(* com.neet_question_api.api.controller.ApiController.getperformanceMatrix(..)) && " +
            "!execution(* com.neet_question_api.api.controller.ApiController.storeStudentQuizHsitory(..)) && " +
            "!execution(* com.neet_question_api.api.controller.ApiController.getRecentQuizzes(..)) && " +
            "!execution(* com.neet_question_api.api.controller.ApiController.getChaptersList(..)) && " +
            "!execution(* com.neet_question_api.api.controller.ApiController.storeFeedback(..)) && " +
            "!execution(* com.neet_question_api.api.controller.ApiController.storeReportedQuestions(..)) && " +
            "!execution(* com.neet_question_api.api.controller.ApiController.getQuizDetails(..)) && " +
            "!execution(* com.neet_question_api.api.controller.ApiController.storeQuizDetails(..)) && " +
            "!execution(* com.neet_question_api.api.controller.ApiController.getStudentId(..)) && " +
            "!execution(* com.neet_question_api.api.controller.ApiController.getUniqueUserId(..))",returning = "result")
    public void loggingApiCall(JoinPoint joinPoint, Object result) throws IOException {
        // Fetching the HttpServletRequest for each method invocation
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        System.out.println(result);
        // Capture method name, time, and other details
        String methodName = joinPoint.getSignature().getName();
        LocalDateTime currentTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedTime = currentTime.format(formatter);
        String requestURI = request.getRequestURI();
        String httpMethod = request.getMethod();
        String apiKey = request.getHeader("x-api-key");

        // Logging and saving API call details
//        logger.info("Time: {} | HTTP Method: {} | API Method: {} | URI: {}", formattedTime, httpMethod, methodName, requestURI);

       ApiLogs apiLogs = new ApiLogs();
        apiLogs.setHttpMethod(httpMethod);
        apiLogs.setTimestamp(Timestamp.valueOf(formattedTime));
        apiLogs.setRequestUri(requestURI);
        apiLogs.setUserId((String) request.getAttribute("userId"));
        apiLogs.setMethodName(methodName);
        apiLogs.setApi_key(apiKey);
        apiLogs.setQuestion_count(getQuestionCount(result));

        apiLogRepository.save(apiLogs);
    }

    @Around("execution(* com.neet_question_api.api.controller.ApiController.*(..))")
    public Object validateApiKey(ProceedingJoinPoint joinPoint) throws Throwable {
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        String apiKey = request.getHeader("x-api-key");
        String userId = apiUsersRepo.findUserIdByApiKey(apiKey);
        if (userId == null) {
            JsonNode responseJson = objectMapper.createObjectNode()
                    .put("error", "Invalid API Key");
            return responseJson;
        }else{
            request.setAttribute("userId", userId);;
            return joinPoint.proceed();
        }
    }

    public int getQuestionCount(Object result) throws IOException {
        JsonNode rootNode = objectMapper.readTree(result.toString());
        int questionCount = 0;
        if (rootNode instanceof ArrayNode){
            questionCount= rootNode.get(0).get("question_count").asInt();
        }else{
             questionCount = rootNode.get("question_count").asInt();
        }
        return questionCount;
    }


}
