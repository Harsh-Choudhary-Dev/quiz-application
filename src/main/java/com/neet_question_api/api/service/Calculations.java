package com.neet_question_api.api.service;

import com.neet_question_api.api.modal.AnswerValidation;
import com.neet_question_api.api.modal.ChapterIds;
import com.neet_question_api.api.modal.PerformanceSummary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class Calculations {
    @Autowired
    private DynamicTables dynamicTables;

    public List<Integer> distribute(int x, int y) {
        // x = limit, y = total chapters

        List<Integer> distribution = new ArrayList<>();
        int baseValue = x / y; // Base value for each chapter
        int remainder = x % y; // Remaining value to distribute
//        System.out.println(remainder);
//        System.out.println(baseValue);
        // Distribute the base value
        for (int i = 0; i < y; i++) {
            distribution.add(baseValue);

        }

        // Distribute the remainder
        for (int i = 0; i < remainder; i++) {
            distribution.set(i, distribution.get(i) + 1);
        }
//        System.out.println(distribution);
        List<Integer> result = distribution.stream().filter(value -> value != 0).toList();

        return result;
    }



    public int getCorrectAnswerNumber(String option){
        String [] alpha_options = {"(a)","(b)","(c)","(d)"};
//        System.out.println(option);
        List<String> alphaOptionsList = Arrays.asList(alpha_options);
        return alphaOptionsList.indexOf(option)+1;
    }

    public int totalSum(Map<String, Integer> chapterScores){
        int totalSum = 0;
        for (int value : chapterScores.values()) {
            totalSum += value;
        }
        return totalSum;
    }

    public Map<String, Object> calculateCorrectAnsChapterWise(List<AnswerValidation>ansDetails){
        Map<String, Integer> chapterScores = new HashMap<>();
        Map<String, Integer> chIdCount = new HashMap<>();
        Map<String, Object> result = new HashMap<>(chapterScores);
        for(AnswerValidation ans : ansDetails){
            int correct_answer = Integer.parseInt(dynamicTables.fetchAnswerByIdFromTable(ans.getCh_id(),ans.getQuestion_id()));
//            int correctAnswer = getCorrectAnswerNumber(correct_answer);
            if(correct_answer==(ans.getSelected_ans())){
                chapterScores.put(ans.getCh_id(), chapterScores.getOrDefault(ans.getCh_id(), 0) + 1);
            }else {
                chapterScores.put(ans.getCh_id(), chapterScores.getOrDefault(ans.getCh_id(), 0));
            }
            chIdCount.put(ans.getCh_id(), chIdCount.getOrDefault(ans.getCh_id(), 0) + 1);
        }
        int total_marks = totalSum(chapterScores);
        result.put("total_marks",total_marks);
        result.put("correct_question_answered",chapterScores);
        result.put("total_questions",ansDetails.size());
        result.put("score(%)",(total_marks*100)/ansDetails.size());
        result.put("student_id",ansDetails.get(0).getStudent_id());
        result.put("chapterwise_total_question",chIdCount);
        return result;
    }
public List<ChapterIds> calculateQuestionDistribution(List<PerformanceSummary> studentPerformance, int totalQuestions){
    Map<String, Double> weights = new HashMap<>();
//    System.out.println(studentPerformance);
//    Map<String, Integer> questionDistribution = new HashMap<>();
    double totalWeight = 0.0;
    int sum = 0;
    List<ChapterIds> questionDistribution = new ArrayList<>();
        for(PerformanceSummary performanceSummary : studentPerformance){
//            System.out.println("accuracy score");
            double accuracy = performanceSummary.getAvg_accuracy();
            String chapter_id = performanceSummary.getChapter_id();
            double weight = 1 - (accuracy / 100.0);
            weights.put(chapter_id,weight);
            totalWeight += weight;
        }
//    System.out.println(weights);
        for(Map.Entry<String, Double> weight:weights.entrySet()){
//            System.out.println(weight.getKey());
//            System.out.println(weight.getValue());
            int assignedQuestions = (int) Math.floor((weight.getValue() / totalWeight) * totalQuestions);
//            System.out.println(assignedQuestions);
            sum +=assignedQuestions;
            if(assignedQuestions!=0){
                questionDistribution.add(new ChapterIds(weight.getKey(), String.valueOf(assignedQuestions)));
            }
//            System.out.println(questionDistribution.size());
        }
    int pendingQues = totalQuestions - sum;
        if(pendingQues>0){
            String lastKey = new ArrayList<>(weights.keySet()).get(weights.size() - 1);
//            System.out.println(lastKey);
            questionDistribution.add(new ChapterIds(lastKey, String.valueOf(pendingQues)));
//            System.out.println(questionDistribution);
        }

    return (questionDistribution);
}

}
