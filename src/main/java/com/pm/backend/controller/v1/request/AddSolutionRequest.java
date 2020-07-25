package com.pm.backend.controller.v1.request;

import com.pm.backend.model.v1.group.GroupModel;
import com.pm.backend.model.v1.solution.SolutionLanguage;
import com.pm.backend.model.v1.user.UserModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Getter
@Accessors(chain = true)
@NoArgsConstructor
public class AddSolutionRequest {
    private UserModel user;
    private GroupModel group;
    private int questionId;
    private String solutionCode;
    private SolutionLanguage solutionLanguage;
}
