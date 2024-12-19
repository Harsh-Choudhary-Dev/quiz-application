package com.neet_question_api.api.modal;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;

@Entity
@Component
@Data
@Table(name = "api_log")
@Scope("prototype")
public class ApiLogs {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "method_name", nullable = false)
    private String methodName;

    @Column(name = "request_uri", nullable = false)
    private String requestUri;

    @Column(name = "http_method")
    private String httpMethod;

    @Column(name = "timestamp", nullable = false, updatable = false)
    private Timestamp timestamp;

    @Column(name = "userid")
    private String userId;

    @Column(name = "question_count")
    private int question_count;
    private String api_key;
}
