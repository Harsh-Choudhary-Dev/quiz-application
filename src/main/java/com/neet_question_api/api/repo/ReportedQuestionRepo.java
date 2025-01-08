package com.neet_question_api.api.repo;

import com.neet_question_api.api.modal.Chapter_id;
import com.neet_question_api.api.modal.ReportedQuestions;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ReportedQuestionRepo extends JpaRepository<ReportedQuestions, Integer> {

}
