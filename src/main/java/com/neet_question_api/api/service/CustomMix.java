package com.neet_question_api.api.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.neet_question_api.api.modal.ChapterIds;
import com.neet_question_api.api.modal.Chapter_id;
import com.neet_question_api.api.modal.Questions;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
@Data
public class CustomMix {
    @Autowired
    private DynamicTables dynamicTables;

    @Autowired
    private final ObjectMapper objectMapper;
    @Autowired
    private Calculations calc;

    @PersistenceContext
    private EntityManager entityManager;

    public List<JsonNode> loopJson(List<ChapterIds> ch_ids){
//        ObjectMapper objectMapper = new ObjectMapper();
        ArrayNode allQuestionsArray = objectMapper.createArrayNode();

        for (ChapterIds chapter : ch_ids) {
            List<Questions> questions = fetchQuestions(chapter.getChapter_id(), chapter.getQuestion_number());
            for (Questions question : questions) {
                ObjectNode questionJson = objectMapper.createObjectNode();
                questionJson.put("chapter_id", chapter.getChapter_id());
                questionJson.put("id", question.getQuestion_id());
                questionJson.put("question", question.getQuestion());
                questionJson.put("option1", question.getOption1());
                questionJson.put("option2", question.getOption2());
                questionJson.put("option3", question.getOption3());
                questionJson.put("option4", question.getOption4());
                questionJson.put("correct-ans", question.getAnswer());
                allQuestionsArray.add(questionJson);
            }
        }
        return Collections.singletonList(allQuestionsArray);
    }


    public List fetchQuestions(String table_name, String limit){
        return dynamicTables.fetchRandomQuestionsFromTable(table_name,limit);
    }



    public JsonNode wrapJsonInDataKey(List<JsonNode> dataList, int question_count) {
        ObjectNode wrappedJson = objectMapper.createObjectNode();
        if(dataList instanceof ArrayList<?>){
            wrappedJson.set("data", objectMapper.valueToTree(dataList));
            wrappedJson.put("question_count", question_count);
        }else{
            wrappedJson.set("data", objectMapper.valueToTree(dataList.get(0)));
            wrappedJson.put("question_count", question_count);
        }

        return wrappedJson;
    }

public List<Questions> questionsForAllChapters(List<Chapter_id> chapter_ids, long total_chapters){
    List<Questions> questionList = new ArrayList<>();
        List<Integer> distribution = calc.distribute(90, (int) total_chapters);
        System.out.println(distribution);
        int i=0;
    for (Chapter_id chapter : chapter_ids){
        if (i>distribution.size()-1){
            break;
        }
        String table_limit = String.valueOf(distribution.get(i));
        String table_name = chapter.getCh_id();
        List quesList = dynamicTables.fetchRandomQuestionsFromTable(table_name,table_limit,"answer");
        questionList.addAll(quesList);
        i++;

    }
    return questionList;
}
public JsonNode addChapterIds(List<Questions> quesList, String chapter_id){
//    System.out.println(quesList.getClass().getName());
    ArrayNode allQuestionsArray = objectMapper.createArrayNode();
//    System.out.println(quesList);
    for(Questions question:quesList){
        ObjectNode questionJson = objectMapper.createObjectNode();
        questionJson.put("chapter_id", chapter_id);
        questionJson.put("id", question.getQuestion_id());
        questionJson.put("question",question.getQuestion() );
        questionJson.put("option1", question.getOption1());
        questionJson.put("option2", question.getOption2());
        questionJson.put("option3", question.getOption3());
        questionJson.put("option4", question.getOption4());
        questionJson.put("correct-ans", question.getAnswer());
        allQuestionsArray.add(questionJson);
    }
    System.out.println(allQuestionsArray.size());
    System.out.println(allQuestionsArray);

    return wrapJsonInDataKey(Collections.singletonList(allQuestionsArray),allQuestionsArray.size());
}

}
