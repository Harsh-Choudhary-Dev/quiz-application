package com.neet_question_api.api.modal;


import jakarta.persistence.*;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;

@Data
@Entity
@Table(name = "api_keys")
public class ApiKeys {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Transient
    private Timestamp created_at;
    private String api_name;
    private String user_id;
    private String api_id;
    @Transient
    private String api_key;
}
