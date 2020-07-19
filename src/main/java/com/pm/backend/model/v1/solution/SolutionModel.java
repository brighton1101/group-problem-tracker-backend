package com.pm.backend.model.v1.solution;

import com.pm.backend.model.v1.group.GroupModel;
import com.pm.backend.model.v1.question.QuestionModel;
import com.pm.backend.model.v1.user.UserModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import static com.pm.backend.model.v1.SchemaVersion.v;


@Data

@Document(collection= "solutions")
public class SolutionModel {
    
    @ApiModelProperty(hidden = true)
    private String id;

    @DBRef(lazy=true)
    private UserModel user;

    @DBRef(lazy=true)
    private GroupModel group;
    private QuestionModel question;
    private String solutionCode;
    private SolutionLanguage solutionLanguage;

    @ApiModelProperty(hidden = true)
    private int schemaVersion;
}
