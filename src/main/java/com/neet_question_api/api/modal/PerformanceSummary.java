package com.neet_question_api.api.modal;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Entity
@Table(name = "chapter_performance")
@Component
public class PerformanceSummary {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String chapter_id;
    private String chapter_name;
    private String student_id;
    private String first_name;
    private int total_score;
    private int total_questions;
    private double avg_score;
    private double avg_accuracy;
}
