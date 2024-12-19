package com.neet_question_api.api.modal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
@Data
@NoArgsConstructor
public class ApiResponse {
    private String message;
    private String apiKey;
    private String api_id;

}
