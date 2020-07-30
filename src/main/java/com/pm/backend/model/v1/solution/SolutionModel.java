package com.pm.backend.model.v1.solution;

import com.pm.backend.model.v1.group.GroupModel;
import com.pm.backend.model.v1.user.UserModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Data
@Accessors(chain = true)
@Document(collection = "solutions")
public class SolutionModel {

    @Id
    private String id;
    private UserModel user;
    private GroupModel group;
    private int questionId;
    private String solutionCode;
    private SolutionLanguage solutionLanguage;

    @ApiModelProperty(hidden = true)
    private int schemaVersion;
}
