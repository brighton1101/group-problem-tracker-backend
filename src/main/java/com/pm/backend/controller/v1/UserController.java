package com.pm.backend.controller.v1;

import com.pm.backend.model.v1.group.GroupModel;
import com.pm.backend.model.v1.user.UserModel;
import com.pm.backend.repository.UserRepository;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

	@PostMapping
	public ResponseEntity addUser(@NotNull @RequestBody UserModel user) {
		user.setSchemaVersion(v);
		userRepository.save(user);

		return ResponseEntity.ok("Added user: " + user.getId());
	}

	//TODO fix this to use a response object instead of returning the model
	@GetMapping("/{id}")
	public Optional<UserModel> getUser(@PathVariable String id) {
		return userRepository.findById(id);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity deleteUser(@PathVariable String id) {
		userRepository.deleteById(id);
		return ResponseEntity.ok("Deleted user with id: " + id);
	}


	
	/*
	@GetMapping
	public String hello(@RequestParam(value = "name", defaultValue = "world") String name) {
		return String.format("Hello, %s", name);
	}
	}*/



} 