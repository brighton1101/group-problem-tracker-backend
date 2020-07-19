package com.pm.backend.controller.v1;


import com.pm.backend.model.v1.group.GroupModel;
import com.pm.backend.repository.GroupRepository;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

import static com.pm.backend.controller.v1.SchemaVersion.v;

@RestController
@RequestMapping("v1/groups")
public class GroupController {

    @Autowired
    private GroupRepository groupRepository;

    @PostMapping("/addGroup")
    public String addGroup(@NotNull @RequestBody GroupModel group) {
        group.setSchemaVersion(v);
        return "Added group: "+  groupRepository.save(group).getId();
        //GroupModel  g = groupRepository.save(group);

       // return "Added group: " + g;
    }

    @GetMapping("/getGroup/{id}")
    public Optional<GroupModel> getGroup(@PathVariable String id) {
        return groupRepository.findById(id);
    }

}
