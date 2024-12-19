package com.neet_question_api.api.modal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity

public class Students {
    @Id
    private String student_id;
    private String first_name;
    private String email;
}
