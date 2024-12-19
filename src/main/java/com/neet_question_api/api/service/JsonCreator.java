package com.neet_question_api.api.service;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.neet_question_api.api.modal.ChapterIds;
import com.neet_question_api.api.modal.Questions;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;


@Component
@Data
public class JsonCreator {

    @Autowired
    private final ObjectMapper objectMapper;




    public List<ChapterIds> createJsonFromChapters(List<String> chapterNames, List<Integer> values) {
        List<ChapterIds> chapters = new ArrayList<>();
        for (int i = 0; i < chapterNames.size(); i++) {
            chapters.add(new ChapterIds(chapterNames.get(i),Integer.toString(values.get(i))));
        }

        return chapters;
    }

}
