package com.neet_question_api.api.modal;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Table(name = "chapter_id")
@Entity
public class Chapter_list {
    private String ch_name;
    private String status;
    private String ch_id;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
}
