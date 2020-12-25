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
            @RequestHeader(value = "Authorization") String authHeader) {

        logger.info("Here {} ", authHeader);

        if (authHeader != null && authHeader.startsWith("Basic")) {
            String credsBase64 = authHeader.replace("Basic", "").trim();
            byte[] credsBytes = Base64.getDecoder().decode(credsBase64);
            String credsString = new String(credsBytes, StandardCharsets.UTF_8);

            String[] creds = credsString.split(":", 2);
            String userName = creds[0];
            String password = creds[1];

            logger.info("Got {} : {}", userName, password);

            try {
                KeyCloakUser user = new KeyCloakUser()
                        .setUserName(userName)
                        .setPassword(password);


                KeyCloakUserAdapter keyCloakUserAdapter = KeyCloakUserAdapter.getInstance(new KeyCloakContext(env));
                AccessToken accessToken = keyCloakUserAdapter.login(user);

                return ResponseEntity.ok(accessToken);
                //return ResponseEntity.badRequest().build();
            } catch (Exception e) {
                //exception thrown by keycloak
                logger.error(e.getMessage());
                //🥰
                return new ResponseEntity(HttpStatus.I_AM_A_TEAPOT);
            }

        } else {
            //This means the auth header is sent incorrectly
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("refreshToken")
    public ResponseEntity refreshToken(@RequestBody String refreshToken) {
        logger.info("refresh token request");

        try {
            KeyCloakUserAdapter keyCloakUserAdapter = KeyCloakUserAdapter.getInstance(new KeyCloakContext(env));
            AccessToken accessToken = keyCloakUserAdapter.refresh(refreshToken);

            return ResponseEntity.ok().body(accessToken);

        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.badRequest().build();
        }

    }

    @PostMapping("/users")
    public ResponseEntity register(@RequestBody KeyCloakUser user) {
        logger.info("trying to register a user");
        try {
            KeyCloakUserAdapter keyCloakUserAdapter = KeyCloakUserAdapter.getInstance(new KeyCloakContext(env));
            KeyCloakUser registeredUser = keyCloakUserAdapter.register(user);

            return ResponseEntity.ok().body(registeredUser);

        } catch (Exception e) {
            //some other bad error
            logger.error(e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping(value="/users/{userId}",
                produces = {"application/json"})
    public ResponseEntity getUserProfile(@PathVariable String userId) {

        logger.info("Get profile for user {}", userId);

        try {
            KeyCloakUserAdapter keyCloakUserAdapter = KeyCloakUserAdapter.getInstance(new KeyCloakContext(env));
            KeyCloakUser user = keyCloakUserAdapter.getUserById(userId);

            return ResponseEntity.ok(user);
        }catch (KeyCloakException e) {
            //the user doesn't exist
            return ResponseEntity.notFound().build();

        }catch (Exception e) {
            //some other error
            logger.error(e.getMessage());
            return ResponseEntity.badRequest().build();

        }

    }

    @GetMapping(value = "/logout")
    public ResponseEntity logout(@RequestHeader(value="X-UserId") String userId) {

        logger.info("logout request for {}", userId);
        try {
            KeyCloakUserAdapter keyCloakUserAdapter = KeyCloakUserAdapter.getInstance(new KeyCloakContext(env));
            keyCloakUserAdapter.logout(userId);
        } catch (KeyCloakException e) {
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
