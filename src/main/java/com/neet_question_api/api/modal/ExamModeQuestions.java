package com.neet_question_api.api.modal;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Data
@Entity
@Scope("prototype")
public class ExamModeQuestions {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int question_id;
    private String question;
    //    @Transient
//    private String question_ids;
    private String option1;
    private String option2;
    private String option3;
    private String option4;
    @Transient
    private String chapter_id;
    @Transient
    private String answer = "Check answer from validate endpoint";

}
