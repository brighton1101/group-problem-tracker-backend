package com.pm.backend.keycloak;



import com.pm.backend.security.*;
import com.pm.backend.security.authz.KeyCloakAuthzAdapter;
import com.pm.backend.security.representations.*;
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
    public static void setup() throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException, KeyCloakException {
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

    @Test()
    public void loginTest(){
        logger.info("Logintest");
        try {
            //KeyCloakUser user = new KeyCloakUser().setUserName("test2").setPassword("test2");
            KeyCloakUser user = new KeyCloakUser().setUserName("test1").setPassword("test1");
            AccessToken token = keyCloakUserAdapter.login(user);

            logger.info("AccessToken is {}", token.toString());

            //keyCloakAuthzAdapter.checkUserAccessToGroup(user, "bobbabois");
            keyCloakAuthzAdapter.queryResourcePermissions("group1", token.getToken());

            //keyCloakAuthzAdapter.checkTokenAccessToGroup(token.getToken(), "group1");

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


        }catch(KeyCloakException e) {
            logger.error("Exception in logout test" + e.toString());
            e.printStackTrace();
        }
    }

    //@Test()
    public void registerTest(){
        logger.info("Start register tst");

        KeyCloakUser bob = new KeyCloakUser()
                .setFirstName("am")
                .setLastName("tam")
                //.setPassword("lahey")
                //.setUserName("bob123");
                .setPassword("test4")
                .setUserName("test4");
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


    //@Test()
    public void createGroupTest(){
        logger.info("createGroupTest");
        try {

            KeyCloakResource group = new KeyCloakGroup()
                    .setName("group1")
                    .setOwnerId("0835b82a-8f53-403d-9c8e-2decde188fcb")
                    .setType("urn:login-app:resources:group");
            group.addScope("group:view");
            group.addUri("/groups/group1");


            keyCloakAuthzAdapter.createGroup(group);

        }catch(Exception e) {
            logger.error("Exception in createGroupTest " + e);
            e.printStackTrace();
        }
    }




}
