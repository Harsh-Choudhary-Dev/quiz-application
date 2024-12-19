package com.neet_question_api.api.modal;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Component

public class ChapterIds {
    private String chapter_id;
    private String question_number;

}
