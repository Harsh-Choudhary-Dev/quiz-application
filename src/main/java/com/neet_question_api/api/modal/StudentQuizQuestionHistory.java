package com.neet_question_api.api.modal;


import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "Student_Quiz_History")
public class StudentQuizQuestionHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String history_id;
    private String student_id;
    private String quiz_id;
    private String question_id;
    private String student_answer;
    private boolean is_correct;
    private String chapter_name;
}
