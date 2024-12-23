package com.neet_question_api.api.repo;

import com.neet_question_api.api.modal.Chapter_id;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface Chapter_ids extends JpaRepository<Chapter_id, Integer> {
    List<Chapter_id> findAll();
    long count();
    // Custom query to get 4 random chapters
    @Query(value = "SELECT ch_id FROM chapter_id WHERE status = 'success' ORDER BY RAND() LIMIT 4", nativeQuery = true)
    List<String> findRandomChapterIds();
}
