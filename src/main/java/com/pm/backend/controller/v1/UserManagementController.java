package com.pm.backend.controller.v1;


import com.pm.backend.security.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * This controller is distinct from UserController because it handles all the auth for user login/logout/register
 */

@RestController
@RequestMapping("/v1/usermanagement")
public class UserManagementController {

    Logger logger = LoggerFactory.getLogger(UserManagementController.class);


    @Autowired
    private Environment env;


    @GetMapping("/login")
    public ResponseEntity login(
            @RequestHeader(value = "Authorization") String authHeader
    ) {

        if (authHeader != null && authHeader.startsWith("Basic")) {
            String credsBase64 = authHeader.replace("Basic", "");
            byte[] credsBytes = Base64.getDecoder().decode(credsBase64);
            String credsString = new String(credsBytes, StandardCharsets.UTF_8);

            String[] creds = credsString.split(":", 1);
            String userName = creds[0];
            String password = creds[1];

            try {
                KeyCloakUser user = new KeyCloakUser()
                        .setUserName(userName)
                        .setPassword(password);

                KeyCloakUserAdapter keyCloakUserAdapter = KeyCloakUserAdapter.getInstance(new UserContext(env));
                AccessToken accessToken = keyCloakUserAdapter.login(user);

                //return ResponseEntity.ok(accessToken);
                return ResponseEntity.badRequest().build();
            } catch (Exception e) {
                //exception thrown by keycloak
                logger.error(e.getMessage());

                return ResponseEntity.badRequest().build();
            }

        } else {
            //This means the auth header is sent incorrectly
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/users")
    public ResponseEntity register(@RequestBody KeyCloakUser user) {
        try {
            KeyCloakUserAdapter keyCloakUserAdapter = KeyCloakUserAdapter.getInstance(new UserContext(env));
            KeyCloakUser registeredUser = keyCloakUserAdapter.register(user);

            return ResponseEntity.ok().body(registeredUser);

        } catch (Exception e) {
            //some other bad error
            logger.error(e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/logout")
    public ResponseEntity logout(@RequestBody String userId) {
        try {
            KeyCloakUserAdapter keyCloakUserAdapter = KeyCloakUserAdapter.getInstance(new UserContext(env));
            keyCloakUserAdapter.logout(userId);
        } catch (UserException e) {
            //somehow the logout failed
            return ResponseEntity.badRequest().body(e.getReason());
        } catch (Exception e) {
            //some other bad error
            logger.error(e.getMessage());
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().build();

    }
}
