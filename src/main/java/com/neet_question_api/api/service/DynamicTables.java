package com.neet_question_api.api.service;

import com.neet_question_api.api.modal.ExamModeQuestions;
import com.neet_question_api.api.modal.Questions;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class DynamicTables {
    @Autowired
    private EntityManager entityManager;

    public List fetchRandomQuestionsFromTable(String tableName, String limit) {
        try {
            String query = String.format("SELECT * FROM %s ORDER BY RAND() LIMIT %s", tableName, limit);
            return entityManager.createNativeQuery(query, Questions.class).getResultList();
        } catch (Exception e) {
            System.out.println(e);
        }
        return Collections.singletonList(new Questions());
    }

    public List fetchRandomQuestionsFromTable(String tableName, String limit, String excludedFeild) {
        List<ExamModeQuestions> questionsList = new ArrayList<>();
        try {
            String query = String.format("SELECT question_id,question,option1,option2,option3,option4 FROM %s ORDER BY RAND() LIMIT %s", tableName, limit);
            questionsList= entityManager.createNativeQuery(query, ExamModeQuestions.class).getResultList();
            for (ExamModeQuestions question : questionsList) {
                question.setChapter_id(tableName);
            }
            return questionsList;
        } catch (Exception e) {
            System.out.println(e);
        }
        return Collections.singletonList(new ExamModeQuestions());
    }


    public String fetchAnswerByIdFromTable(String tableName, int id) {
        try {
            // Change the query to select only the "answere" field where id matches
            String query = String.format("SELECT answer FROM %s WHERE question_id = :id", tableName);

            // Create a query and set the parameter for id
            String answer = (String) entityManager.createNativeQuery(query)
                    .setParameter("id", id)
                    .getSingleResult();

            return answer;
        } catch (Exception e) {
            System.out.println("Error fetching answer: " + e.getMessage());
        }
        return null; // Return null if no answer is found or an error occurs
    }

}

