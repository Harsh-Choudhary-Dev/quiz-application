package com.neet_question_api.api.repo;

import com.neet_question_api.api.modal.StudentQuizQuestionHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentQuizQuestionHistoryRepo extends JpaRepository<StudentQuizQuestionHistory, Integer> {

}
