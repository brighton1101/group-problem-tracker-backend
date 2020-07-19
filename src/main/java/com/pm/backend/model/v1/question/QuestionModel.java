package com.pm.backend.model.v1.question;


import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import static com.pm.backend.model.v1.SchemaVersion.v;

import java.util.List;

@Data

@Document(collection= "questions")
public class QuestionModel {
    @Id
    private int id;
    private String questionTitle;
    private String questionUrl;
    private QuestionDifficulty questionDifficulty;
    private String questionDescription;
    private List<String> questionTopics;
    private final int schemaVersion = v;



}
