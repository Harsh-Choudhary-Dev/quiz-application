package com.neet_question_api.api.modal;


import jakarta.persistence.*;
import lombok.Data;
import org.springframework.context.annotation.Scope;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Data
@Entity
@Scope("prototype")
public class Questions {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="question_id")
    private int question_id;
    private String question;
//    @Transient
//    private String question_ids;
    private String option1;
    private String option2;
    private String option3;
    private String option4;
    private String answer ;
    @Transient
    private String chapter_id ;

}
