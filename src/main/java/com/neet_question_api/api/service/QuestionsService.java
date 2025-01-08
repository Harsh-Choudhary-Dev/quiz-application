package com.neet_question_api.api.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.neet_question_api.api.modal.*;

import com.neet_question_api.api.repo.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;


@Service
@Data
public class QuestionsService {

    @Autowired
    private final ObjectMapper objectMapper;
    @Autowired
    private final StudentQuizResultsRepo studentQuizResultsRepo;

    @Autowired
    private final StudentQuizResults studentQuizResults;

    @Autowired
    private Calculations calculation;
    @Autowired
    private FeedbackRepo feedbackRepo;
    @Autowired
    private CustomMix customMix;
    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private JsonCreator jsonCreator;

    @Autowired
    private UniqueIds uniqueIds;

    @Autowired
    private StudentsRepo studentsRepo;

    @Autowired
    private Chapter_ids chapter_ids;

    @Autowired
    private ReportedQuestionRepo reportedQuestionRepo;

    @Autowired
    private DynamicTables dynamicTables;

    @Autowired
    private PerformanceRepo performanceRepo;
    @Autowired
    private PerformanceMatrix performanceMatrix;
    @Autowired
    private PerformanceSummaryRepo performanceSummaryRepo;
    @Autowired
    private ChapterListRepo chapterListRepo;


    public Object fetchRandomDataFromTable() {
//        List<String> chapters = Arrays.asList(
//                "CH_1", "CH_2", "CH_3", "CH_4", "CH_5",
//                "CH_7", "CH_8", "CH_9", "CH_10", "CH_11",
//                "CH_12", "CH_13", "CH_14", "CH_15", "CH_16",
//                "CH_17", "CH_18", "CH_19", "CH_20", "CH_21"
//        );

        List<String> chapters = chapter_ids.findRandomChapterIds();
        System.out.println(chapters);
        Collections.shuffle(chapters);
        List<ChapterIds> resultList = new ArrayList<>();
        int totalQuestions = 0;

        for (String chapter : chapters.subList(0, 4)) {
            int questionNumber = Math.min((int) (Math.random() * 30) + 1, 50 - totalQuestions);
            totalQuestions += questionNumber;
            resultList.add(new ChapterIds(chapter.toLowerCase(), String.valueOf(questionNumber)));
            if (totalQuestions >= 50) break;
        }
        return fetchCustomMixQuestions(resultList);
    }

    //    public Object fetchRandomDataFromTable(String tableName) {
//        return fetchRandomDataFromTable(tableName,"50");
//    }
    public JsonNode fetchCustomMixQuestions(List<ChapterIds> chapterIdsList) {
        List<JsonNode> ques = customMix.loopJson(chapterIdsList);
        int question_count = ques.get(0).size();
        //System.out.println("harsh is question from fetchCustomMixQuestions"+ques);
        return customMix.wrapJsonInDataKey(ques, question_count);
    }

    public List<JsonNode> fetchAutoMixQuestions(List<String> chapterIdsList, int limit) {
        List<Integer> values = calculation.distribute(limit, chapterIdsList.size());
        List<ChapterIds> chapterJson = jsonCreator.createJsonFromChapters(chapterIdsList, values);
        List<JsonNode> ques = customMix.loopJson(chapterJson);
        int question_count = ques.get(0).size();

        return Collections.singletonList(customMix.wrapJsonInDataKey(ques, question_count));

    }

    public ResponseEntity<Map<String, String>> generateUserId(Students student_name) {
        Map<String, String> response = new HashMap<>();
        student_name.setStudent_id(uniqueIds.generateUserId());
        try {
            studentsRepo.save(student_name);
            response.put("studentId", student_name.getStudent_id());
            return ResponseEntity.ok(response);
        } catch (DataIntegrityViolationException e) {
            response.put("error", "email already exist");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        } catch (Exception e) {
            response.put("error", "Internal error");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        }
    }

    public ObjectNode fetchExamModeQuestions() {
//        5 are missing only 85 instead of 90
        ObjectNode wrappedJson = objectMapper.createObjectNode();
        List<Chapter_id> chapterList = chapter_ids.findAll();
        long total_chapters = chapter_ids.count();
        List<Questions> ques = customMix.questionsForAllChapters(chapterList, total_chapters);
        int question_count = ques.size();
        wrappedJson.set("data", objectMapper.valueToTree(ques));
        wrappedJson.put("question_count", question_count);
        return wrappedJson;
    }

    @Transactional
    public Object checkWrongOrRight(List<AnswerValidation> ansDetails) {
        Map<String, Object> record = calculation.calculateCorrectAnsChapterWise(ansDetails);
        @SuppressWarnings("unchecked")
        Map<String, Integer> correctAnswers = (Map<String, Integer>) record.get("correct_question_answered");
        @SuppressWarnings("unchecked")
        Map<String, Integer> chapterwiseTotalQuestion = (Map<String, Integer>) record.get("chapterwise_total_question");
        for (Map.Entry<String, Integer> entry : correctAnswers.entrySet()) {
            String chapterId = entry.getKey();
            Integer correctAnswer = entry.getValue();
            Integer questionCount = chapterwiseTotalQuestion.get(chapterId);
//            System.out.println("Chapter ID: " + chapterId + ", Correct Answers: " + correctAnswer + " ,question count: "+ questionCount);
            performanceMatrix.setScore(correctAnswer);
            performanceMatrix.setTotal_questions(questionCount);
            performanceMatrix.setStudent_id((String) record.get("student_id"));
            performanceMatrix.setChapter_id(chapterId);
//            System.out.println(performanceMatrix);
            performanceRepo.insertChapterPerformance(performanceMatrix);
        }
        return record;
    }

    public Object getStudentAnalytics(String student_id) {
        return performanceSummaryRepo.findChapterPerformanceByStudentAndInterval(student_id);
    }


    public Object fetchQuestionsBasedOnPerformance(String student_id) {
        List<PerformanceSummary> studentPerformance = performanceSummaryRepo.findChapterPerformanceByStudentAndInterval(student_id);
//        System.out.println(studentPerformance);
        List<ChapterIds> questionDistribution = calculation.calculateQuestionDistribution(studentPerformance, 50);
        List<JsonNode> questions = customMix.loopJson(questionDistribution);
        int question_count = (questions.get(0).size());
        return Collections.singletonList(customMix.wrapJsonInDataKey(questions, question_count)).get(0);
    }

    public Object fetchRecentQuizzes(String student_id) {
        return studentQuizResultsRepo.findTop5ByStudentId(student_id);
    }

    public Object fetchExistingStudent(String email) {
//        System.out.println(email);
        return studentsRepo.findByEmail(email);
    }

    public List<StudentQuizResults> fetchQuizDetails(String quiz_id) {
        return studentQuizResultsRepo.findByQuizId(quiz_id);
    }

    public ResponseEntity<Map<String, String>> storeStudentQuizDetails(StudentQuizResults studentQuizResults) {
        String quiz_id = uniqueIds.generateUserId();
        Map<String, String> response = new HashMap<>();
        studentQuizResults.setQuiz_id(quiz_id);
//        System.out.println(studentQuizResults);
        studentQuizResultsRepo.save(studentQuizResults);
        response.put("status", "Quiz details saved");
        response.put("quiz_id", quiz_id);

        return ResponseEntity.ok(response);
    }

    public ResponseEntity<List<Map<String, Object>>> fetchPerformanceMatrix(String student_id, String interval) {
        System.out.println(student_id);
        List<Object[]> result = studentQuizResultsRepo.findQuizStatsForLastNDays(student_id, Integer.parseInt(interval));

        // Convert the result into a List of Maps (where each map represents a row in the result)
        List<Map<String, Object>> response = result.stream().map(row -> {
            Map<String, Object> map = new HashMap<>();
            map.put("quiz_date", row[0]); // Date of quiz
            map.put("total_score", row[1]); // Total score
            map.put("total_quizzes", row[2]); // Total number of quizzes
            map.put("average_score", row[3]); // Average score
            return map;
        }).collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    public ResponseEntity<Map<String, String>> storeUserFeedback(Feedback feedback) {
        Map<String, String> response = new HashMap<>();
        feedbackRepo.save(feedback);
        response.put("status", "Feedback submitted ");
        return ResponseEntity.ok(response);
    }

    public Map<String, Object> fetchStatsForStudent(String studentId) {
        return studentQuizResultsRepo.getStudentQuizStats(studentId);
    }

    public Object fetchCustomTopicsQuestion(List<ChapterIds> chapter_id) {
        JsonNode ques = customMix.loopJson(chapter_id).get(0);
        System.out.println(ques.size());
        return customMix.wrapJsonInDataKey(Collections.singletonList(ques), ques.size());
    }

    public List<Chapter_list> fetchChapterList() {
        return chapterListRepo.findAllChaptersWithSuccessStatus();
    }


    public ResponseEntity<Map<String, Object>> storeReportedQuestionsData(List<ReportedQuestions> reportedQuestions) {
        Map<String, Object> response = new HashMap<>();
        try {
            reportedQuestionRepo.save(reportedQuestions.get(0));
            response.put("status", "success");
            response.put("message", "Reported question data saved successfully.");
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", "Failed to save reported question data.");
            response.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

}
