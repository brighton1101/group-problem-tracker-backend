package com.pm.backend.controller.v1;


import com.pm.backend.security.*;
import com.pm.backend.security.representations.AccessToken;
import com.pm.backend.security.representations.KeyCloakException;
import com.pm.backend.security.representations.KeyCloakUser;
import com.pm.backend.security.representations.KeyCloakContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Optional;

/**
 * This controller is distinct from UserController because it handles all the auth for user login/logout/register
 */

@RestController
@RequestMapping("/v1/usermanagement")
public class UserManagementController {

    Logger logger = LoggerFactory.getLogger(UserManagementController.class);


    @Autowired
    private Environment env;

    @Autowired
    private UserAuth authManager;


    @GetMapping("/login")
    public ResponseEntity login(
            @RequestHeader(value = "Authorization") String authHeader) {
        return parseUserCreds(authHeader)
                .map(kcuser -> authManager.login(kcuser))
                .map(at -> ResponseEntity.ok().body(at))
                .orElse(ResponseEntity.badRequest().build());
    }

    @PostMapping("refreshToken")
    public ResponseEntity refreshToken(@RequestBody String refreshToken) {
        return authManager.refresh(refreshToken)
                .map(responseToken -> ResponseEntity.ok().body(responseToken))
                .orElse(ResponseEntity.badRequest().build());
    }

    @PostMapping("/users")
    public ResponseEntity register(@RequestBody KeyCloakUser user) {
        return authManager.register(user)
                .map((responseUser) -> ResponseEntity.ok().body(responseUser))
                .orElse(ResponseEntity.badRequest().build());
    }

    @GetMapping(value="/users/{userId}",
                produces = {"application/json"})
    public ResponseEntity getUserProfile(@PathVariable String userId) {
        return authManager.getUserById(userId)
                .map(kcuser -> ResponseEntity.ok().body(kcuser))
                .orElse(ResponseEntity.badRequest().build());
    }

    @GetMapping(value = "/logout")
    public ResponseEntity logout(@RequestHeader(value="X-UserId") String userId) {
        boolean res = authManager.logout(userId);
        if (res == false) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().build();
    }

    private Optional<KeyCloakUser> parseUserCreds(String authHeader) {
        if (authHeader == null || !(authHeader.startsWith("Basic"))) {
            return Optional.empty();
        }
        String credsString;
        try {
            byte[] decodedBytes = Base64.getDecoder().decode(
                    authHeader.replace("Basic", "").trim()
            );
            credsString = new String(decodedBytes, StandardCharsets.UTF_8);
        } catch (IllegalArgumentException e) {
            return Optional.empty();
        }
        String[] creds = credsString.split(":", 2);
        if (creds.length != 2) {
            return Optional.empty();
        }
        return Optional.of(new KeyCloakUser()
                .setUserName(creds[0])
                .setPassword(creds[1]));
    }
}
