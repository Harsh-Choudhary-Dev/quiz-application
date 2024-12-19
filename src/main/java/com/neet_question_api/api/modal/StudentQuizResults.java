package com.neet_question_api.api.modal;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.stereotype.Component;

@Data
@Entity
@Component
@Table(name = "student_quiz_results")
public class StudentQuizResults {
    @Id
    private String quiz_id;
    private String student_id;
    private int score;
    private int total_marks;
    private int total_questions;
    private String quiz_type;
    private int time_taken;
    @CreationTimestamp
    private String created_at;
    private String chapter_ids;
}
