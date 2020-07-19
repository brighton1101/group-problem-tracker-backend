package com.pm.backend.controller.v1;

import com.pm.backend.controller.v1.request.AddUserRequest;
import com.pm.backend.model.v1.user.UserModel;
import com.pm.backend.repository.UserRepository;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

import static com.pm.backend.controller.v1.SchemaVersion.v;

/**
 * High level controller for service that deals with managing
 * user functionality. Some pieces of functionality that should
 * get routed through here:
 * - Create group endpoint
 * - Join group endpoint
 * - Leave group endpoint
 * - Get group stats?
 */
@RestController
@RequestMapping("/v1/users")
class UserController {
	
	@Autowired
	private UserRepository userRepository;

	@PostMapping("/addUser")
	public String addUser(@NotNull @RequestBody UserModel user) {
		user.setSchemaVersion(v);
		userRepository.save(user);

		return "Added user: " + user.getId();
	}

    @Autowired
    private UserRepository userRepository;
    //@Autowired
    //private UserGroupRepository userGroupRepository;

    @PostMapping
    public ResponseEntity addUser(@NotNull @RequestBody AddUserRequest addUserRequest) {
        UserModel user = new UserModel().setUserName(addUserRequest.getUserName())
                .setUserPassword(addUserRequest.getUserPassword())
                .setSchemaVersion(v);
        userRepository.save(user);
        return ResponseEntity.ok("Added user: " + user.getId());
    }


    @GetMapping("/{id}")
    public ResponseEntity getUser(@PathVariable String id) {

        Optional<UserModel> user = userRepository.findById(id);
        if (user.isPresent()) {
            UserModel userModel = user.get();
            return ResponseEntity.ok().body(new UserModel().setId(userModel.getId())
                    .setUserName(userModel.getUserName()));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteUser(@PathVariable String id) {
        userRepository.deleteById(id);
        return ResponseEntity.ok("Deleted user with id: " + id);
    }

}