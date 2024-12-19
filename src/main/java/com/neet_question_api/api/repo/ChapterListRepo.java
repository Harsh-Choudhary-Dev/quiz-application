package com.neet_question_api.api.repo;

import com.neet_question_api.api.modal.ApiLogs;
import com.neet_question_api.api.modal.Chapter_list;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChapterListRepo extends JpaRepository<Chapter_list, Integer> {
    @Query(value = "SELECT * FROM chapter_id WHERE status = 'success'", nativeQuery = true)
    List<Chapter_list> findAllChaptersWithSuccessStatus();
}
