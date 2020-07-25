package com.pm.backend.model.v1.solution;

import com.pm.backend.model.v1.group.GroupModel;
import com.pm.backend.model.v1.question.QuestionModel;
import com.pm.backend.model.v1.user.UserModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import static com.pm.backend.model.v1.SchemaVersion.v;


@Data
@Accessors(chain = true)
@Document(collection= "solutions")
public class SolutionModel {
    
    //@ApiModelProperty(hidden = true)
    private String id;

    private String userId;

    private String groupId;
    private int questionId;
    private String solutionCode;
    private SolutionLanguage solutionLanguage;

    @ApiModelProperty(hidden = true)
    private int schemaVersion;
}
