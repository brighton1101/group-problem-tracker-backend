package com.pm.backend.model.v1.question;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data

@Document(collection = "questions")
@Accessors(chain = true)
public class QuestionModel {
    @Id
    private int id;
    private String questionTitle;
    private String questionUrl;
    private QuestionDifficulty questionDifficulty;
    private String questionDescription;
    private List<String> questionTopics;

    @ApiModelProperty(hidden = true)
    private int schemaVersion;
}
