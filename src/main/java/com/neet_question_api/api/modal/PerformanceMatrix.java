package com.neet_question_api.api.modal;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.context.annotation.Scope;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
@Entity
@Component
@Table(name = "chapter_performance")
@Scope("prototype")

public class PerformanceMatrix {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int performance_id;
    private String student_id;
    private String chapter_id;
    private int score;
    private int total_questions;
    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private Timestamp created_at;

}
