package com.neet_question_api.api.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.neet_question_api.api.modal.*;
import com.neet_question_api.api.service.QuestionsService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@Data
@RequestMapping("api/v1")
public class ApiController {

    @Autowired
    private QuestionsService service;

    @PostMapping("/random/chapter-ids")
    public Object getCustomTopicsQuestion(@RequestBody List<ChapterIds> chapter_id) {
        return service.fetchCustomTopicsQuestion(chapter_id);
    }

    @GetMapping("/random-mix")
    public Object getRandomQuestions() {
        return service.fetchRandomDataFromTable();
    }

    @PostMapping("/custom-mix/chapter-ids")
    public JsonNode getAutoMixQuestions(@RequestBody List<ChapterIds> chapterIdsList ) {
        return service.fetchCustomMixQuestions(chapterIdsList);
    }

    @GetMapping("/performance-based-mock")
    public Object getPerformanceBasedMock(@RequestParam String student_id) {
        return service.fetchQuestionsBasedOnPerformance(student_id);
    }

    @PostMapping("/validate-answer")
    public Object wrongOrRight (@RequestBody List < AnswerValidation > ansDetails) {
        return service.checkWrongOrRight(ansDetails);
    }

    @PostMapping("/student-id")
    public Object getUniqueUserId(@RequestBody Students studentName) {
        return service.generateUserId(studentName);
    }

    @GetMapping("/exam-mode")
    public Object getExamModeQuestions() {
        return service.fetchExamModeQuestions();
    }

    @GetMapping("/student-analytics")
    public Object getStudentResult(@RequestParam String student_id) {
        return service.getStudentAnalytics(student_id);
    }

    @GetMapping("/recent-quizzes")
    public Object getRecentQuizzes(@RequestParam String student_id){
        return service.fetchRecentQuizzes(student_id);
    }
    @GetMapping("/student-id")
    public Object getStudentId(@RequestParam String email){
        return service.fetchExistingStudent(email);
    }
    @GetMapping("/quiz-details")
    public Object getQuizDetails(@RequestParam String quiz_id){
            return service.fetchQuizDetails(quiz_id);
    }

    @PostMapping("/quiz-details")
    public Object storeQuizDetails(@RequestBody StudentQuizResults studentQuizResults){
        return service.storeStudentQuizDetails(studentQuizResults);
    }

    @PostMapping("/performance-matrix")
    public Object getperformanceMatrix(@RequestParam String student_id,String interval){
        return service.fetchPerformanceMatrix(student_id,interval);
    }

    @GetMapping("/student-stats")
    public ResponseEntity<Map<String, Object>> getStudentStats(@RequestParam String student_id) {
        Map<String, Object> stats = service.fetchStatsForStudent(student_id);
        return ResponseEntity.ok(stats);
    }
    @PostMapping("/feedback")
    public ResponseEntity<Map<String, String>> storeFeedback(@RequestBody Feedback feedback) {
        return service.storeUserFeedback(feedback);

    }   
    @GetMapping("/chapters")
    public Object getChaptersList() {
        return service.fetchChapterList();

    }
    @PostMapping("/report-questions")
    public Object storeReportedQuestions(@RequestBody List<ReportedQuestions> reportedQuestions) {
        return service.storeReportedQuestionsData(reportedQuestions);
    }

    @PostMapping("/history")
    public Object storeStudentQuizHsitory(@RequestBody List<StudentQuizQuestionHistory> studentQuizQuestionHistories){
        return service.maintinStudentHistory(studentQuizQuestionHistories);
    }

    //    -----------------------------------under development------------------------------------------------------------------------------------------------------------


//    @GetMapping("/question-of-the-day")
//    public Object getQuestionOfTheDay() {
//        return service.generateUserId();
//    }


// @GetMapping("/flashcards")
//    public Object getFlashCards() {
//        return service.generateUserId();
//    }


//    future comming soon still under development
//    @GetMapping("/all-mix/chapter-ids")
//    public String getAllMixQuestions(@RequestParam(required = false, defaultValue = "50") String limit) {
//        return service.fetchAllMixQuestions();
//    }

    }
