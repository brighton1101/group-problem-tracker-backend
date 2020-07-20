package com.pm.backend.controller.v1;

import com.pm.backend.model.v1.group.GroupModel;
import com.pm.backend.model.v1.user.UserGroup;
import com.pm.backend.model.v1.user.UserModel;
import com.pm.backend.repository.UserGroupRepository;
import com.pm.backend.repository.UserRepository;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import static com.pm.backend.controller.v1.SchemaVersion.v;

import java.util.Optional;

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
	//@Autowired
	//private UserGroupRepository userGroupRepository;

	@PostMapping("/addUser")
	public String addUser(@NotNull @RequestBody UserModel user) {
		user.setSchemaVersion(v);
		userRepository.save(user);

		return "Added user: " + user.getId();
	}

	@GetMapping("/getUser/{id}")
	public Optional<UserModel> getUser(@PathVariable String id) {
		return userRepository.findById(id);
	}

	@DeleteMapping("/deleteUser/{id}")
	public String deleteUser(@PathVariable String id) {
		userRepository.deleteById(id);
		return "Deleted user with id: " + id;
	}


	/* Gonna try doing this from groups instead
	@PostMapping("/joinGroup")
	public String joinGroup(@NotNull @RequestBody UserGroup userGroup) {
		userGroupRepository

		return String.format("Hello, newly joined group %s", groupId);
	}*/
	
	
	
	
	
	
	
	
	@GetMapping
	public String hello(@RequestParam(value = "name", defaultValue = "world") String name) {
		return String.format("Hello, %s", name);
	}

	/*This will be done in by group controller
	@PostMapping("/groups")
	public String createGroup() {
		return "Hello, new group";
	}*/



} 