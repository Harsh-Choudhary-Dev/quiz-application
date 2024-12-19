package com.neet_question_api.api.modal;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;

@Entity
@Data

@NoArgsConstructor
@Table(name = "api_users")
public class ApiUsers {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "user_id")
    private String userId;

    private String username;

    private String email;

    public ApiUsers(String userId, String username, String email) {
        this.userId = userId;
        this.username = username;
        this.email = email;
    }

    @Column(name = "api_key")

    private String apiKey;
    private String account_type="free";

    private Timestamp createdAt;

    public ApiUsers(String userId, String email, Timestamp createdAt,String username) {
        this.userId = userId;
        this.email = email;
        this.username = username;
        this.createdAt = createdAt;
    }

    public ApiUsers(String email) {
        this.email = email;
    }
}
