package com.neet_question_api.api.modal;

import jakarta.persistence.*;
import lombok.Data;

import java.sql.Timestamp;

@Entity
@Table(name = "user_feedback")
@Data
public class Feedback {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String student_id;
    private String email;
    private String feedback_text;
    private String feedback_type;
    @Transient
    private Timestamp submitted_at;
}
