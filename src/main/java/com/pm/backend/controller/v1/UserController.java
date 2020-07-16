package com.pm.backend.controller.v1;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

	@GetMapping
	public String hello(@RequestParam(value = "name", defaultValue = "world") String name) {
		return String.format("Hello, %s", name);
	}

	@PostMapping("/groups")
	public String createGroup() {
		return "Hello, new group";
	}

	@PostMapping("/groups/{id}")
	public String joinGroup(@PathVariable("id") String uuid) {
		return String.format("Hello, newly joined group %s", uuid);
	}

} 