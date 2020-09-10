package com.pm.backend.keycloak;



import com.pm.backend.security.*;
import com.pm.backend.security.authz.KeyCloakAuthzAdapter;
import com.pm.backend.security.representations.AccessToken;
import com.pm.backend.security.representations.KeyCloakUser;
import com.pm.backend.security.representations.KeyCloakContext;
import com.pm.backend.security.representations.UserException;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.junit4.SpringRunner;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

import static com.pm.backend.keycloak.MockContext.getMockContext;

@RunWith(SpringRunner.class)
public class KeyCloakUserTests {
    static Logger logger = LoggerFactory.getLogger(KeyCloakUserTests.class);

    protected static KeyCloakUserAdapter keyCloakUserAdapter;
    protected  static KeyCloakAuthzAdapter keyCloakAuthzAdapter;
    protected static KeyCloakContext context;

    @BeforeClass
    public static void setup() throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException, UserException {
        logger.info("Begin setup");
        context = new KeyCloakContext(getMockContext());
        try {
            keyCloakUserAdapter = KeyCloakUserAdapter.getInstance(context);
            keyCloakAuthzAdapter = KeyCloakAuthzAdapter.getInstance();
        }
        catch(Exception e) {
            logger.error("Exception in setup" + e);
            e.printStackTrace();
            throw e;
        }

        logger.info("finished setup");
    }

    //@Test()
    public void loginTest(){
        logger.info("Logintest");
        try {
            AccessToken token = keyCloakUserAdapter.login(new KeyCloakUser().setUserName("test1").setPassword("test1"));

            logger.info("AccessToken is {}", token.toString());

            //token = keyCloakUserAdapter.login("test1", "failed");
        }catch(Exception e) {
            logger.error("Exception in login test" + e);
            e.printStackTrace();
        }
    }

    //@Test()
    public void refreshTokenTest(){
        logger.info("refreshTokenTest");
        try {

            AccessToken token1 = keyCloakUserAdapter.login(new KeyCloakUser().setUserName("test1").setPassword("test1"));
            logger.info("Initial   token is {}", token1.toString());
            AccessToken token2 = keyCloakUserAdapter.refresh(token1.getRefreshToken());

            logger.info("Refreshed token is {}", token2.toString());

        }catch(Exception e) {
            logger.error("Exception in refreshTokenTest " + e);
            e.printStackTrace();
        }
    }

    //@Test()
    public void logoutTest(){
        logger.info("Logouttest");
        try {

            KeyCloakUser user = keyCloakUserAdapter.getUserByName("test1");
            logger.info("User details: " + user);

            //keyCloakUserAdapter.logout(user.getId());


        }catch(UserException e) {
            logger.error("Exception in logout test" + e.toString());
            e.printStackTrace();
        }
    }

    //@Test
    public void registerTest(){
        logger.info("Start register test");

        KeyCloakUser bob = new KeyCloakUser()
                .setFirstName("bob")
                .setLastName("boi")
                .setPassword("lahey")
                .setUserName("bob123");
        try {
            KeyCloakUser user = keyCloakUserAdapter.register(bob);

            logger.info("Successfully registered user: {}", user);
        }catch (Exception e) {
            logger.error("Exception in register test" + e);
            e.printStackTrace();
        }
    }

    //@Test()
    public void getProfileTest(){
        logger.info("getProfileTest");
        try {
            KeyCloakUser user = keyCloakUserAdapter.getUserById("0835b82a-8f53-403d-9c8e-2decde188fcb");

            logger.info("User is {}", user.toString());

            //token = keyCloakUserAdapter.login("test1", "failed");
        }catch(Exception e) {
            logger.error("Exception in getProfileTest " + e);
            e.printStackTrace();
        }
    }


    @Test()
    public void authzTest1(){
        logger.info("authzTest1");
        try {
            //KeyCloakUser user = keyCloakUserAdapter.getUserById("0835b82a-8f53-403d-9c8e-2decde188fcb");

            //logger.info("User is {}", user.toString());

            //token = keyCloakUserAdapter.login("test1", "failed");
        }catch(Exception e) {
            logger.error("Exception in authzTest1 " + e);
            e.printStackTrace();
        }
    }




}
