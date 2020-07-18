package com.pm.backend.model;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


import java.util.List;

@Getter
@Setter
@ToString

@Document(collection= "questions")
public class QuestionModel {
    @Id
    private int id;
    private String questionTitle;
    private String questionUrl;
    private enum QuestionDifficulty {
        EASY, MEDIUM, HARD
    }
    private QuestionDifficulty questionDifficulty;
    private String questionDescription;
    private List<String> questionTopics;



}
