package com.pm.backend.controller.v1;


import com.pm.backend.controller.v1.request.UserCreateGroupRequest;
import com.pm.backend.controller.v1.request.UserJoinGroupRequest;
import com.pm.backend.model.v1.group.GroupModel;
import com.pm.backend.repository.GroupRepository;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

import static com.pm.backend.controller.v1.SchemaVersion.v;

@RestController
@RequestMapping("v1/groups")
public class GroupController {

    @Autowired
    private GroupRepository groupRepository;

    @PostMapping
    public ResponseEntity addGroup(@NotNull @RequestBody UserCreateGroupRequest group) {

        //TODO this could be moved to a mapper
        GroupModel g = new GroupModel().setGroupName(group.getGroupName())
                                        .setGroupOwner(group.getGroupOwner())
                                        .setUsers(group.getUsers())
                                        .setSchemaVersion(v);
        String gid = groupRepository.save(g).getId();

        return ResponseEntity.ok("Added group: " + gid);
    }

    //TODO fix this to use a response object instead of returning the model
    @GetMapping
    public Optional<GroupModel> getGroup(@PathVariable String id) {
        return groupRepository.findById(id);
    }

    @PostMapping("/users")
    public ResponseEntity addUser(@NotNull @RequestBody UserJoinGroupRequest request) {
        return ResponseEntity.ok(groupRepository.findGroupAddUser(request));
    }

}
