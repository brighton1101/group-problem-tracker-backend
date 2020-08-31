package com.pm.backend.controller.v1;


import com.pm.backend.security.AccessToken;
import com.pm.backend.security.KeyCloakUserAdapter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * This controller is distinct from UserController because it handles all the auth for user login/logout/register
 *
 */

@RestController
@RequestMapping("/v1/usermanagement")
public class UserManagementController {


    @GetMapping("/login")
    public ResponseEntity login(
            @RequestHeader(value = "Authorization") String authHeader
    ) {
        try {
            if(authHeader!=null && authHeader.startsWith("Basic")) {
                String credsBase64 = authHeader.replace("Basic", "");
                byte[] credsBytes = Base64.getDecoder().decode(credsBase64);
                String credsString = new String(credsBytes, StandardCharsets.UTF_8);

                String[] creds = credsString.split(":", 1);
                String user = creds[0];
                String password = creds[1];

                //KeyCloakUserAdapter authAdapter = KeyCloakUserAdapter.getInstance();
                //AccessToken accessToken = authAdapter.login(user, password);

                //return ResponseEntity.ok(accessToken);
                return ResponseEntity.badRequest().build();

            } else {
                //This means the auth header is sent incorrectly
                return ResponseEntity.badRequest().build();
            }

        } catch (Exception e) {


            return ResponseEntity.badRequest().build();
        }

    }
}
