package com.neet_question_api.api.modal;


import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "reported_questions")
public class ReportedQuestions {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int report_id; // Primary Key
    private String source_table; // Table where the question is stored
    private int question_id; // ID of the question in the source table
    private String student_id; // ID of the user reporting the issue
    @Transient
    private java.time.LocalDateTime report_date; // Timestamp of the report
    @Transient
    private String status; // Status of the report: 'Pending', 'Reviewed', 'Resolved'

}
