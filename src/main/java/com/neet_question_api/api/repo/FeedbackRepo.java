package com.neet_question_api.api.repo;

import com.neet_question_api.api.modal.ApiKeys;
import com.neet_question_api.api.modal.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeedbackRepo extends JpaRepository<Feedback, Integer> {
}
