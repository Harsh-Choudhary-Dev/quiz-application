package com.neet_question_api.api.repo;

import com.neet_question_api.api.modal.Questions;
import com.neet_question_api.api.modal.Students;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentsRepo extends JpaRepository<Students, String> {
    List<Students> findByEmail(String email);

    long count();
}
