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

    @GetMapping("{id}")
    public ResponseEntity getGroup(@PathVariable String id) {

        Optional<GroupModel> group = groupRepository.findById(id);
        if (group.isPresent()) {
            GroupModel groupModel = group.get();
            return ResponseEntity.ok().body(new GroupModel().setGroupName(groupModel.getGroupName())
                    .setGroupOwner(groupModel.getGroupOwner())
                    .setUsers(groupModel.getUsers())
                    .setId(groupModel.getId()));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/users")
    public ResponseEntity addUser(@NotNull @RequestBody UserJoinGroupRequest request) {
        return ResponseEntity.ok(groupRepository.findGroupAddUser(request));
    }

    @PostMapping("addUser")
    public String addUser(@NotNull @RequestBody UserJoinGroupRequest request) {
        return groupRepository.findGroupAddUser(request.getGroupId(), request.getUserId());
    }

}
