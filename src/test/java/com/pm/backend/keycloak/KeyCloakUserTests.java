package com.pm.backend.keycloak;



import com.pm.backend.security.AccessToken;
import com.pm.backend.security.KeyCloakUserAdapter;
import com.pm.backend.security.UserContext;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.test.context.junit4.SpringRunner;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

import static com.pm.backend.keycloak.MockContext.getMockContext;

@RunWith(SpringRunner.class)
public class KeyCloakUserTests {
    static Logger logger = LoggerFactory.getLogger(KeyCloakUserTests.class);

    protected static KeyCloakUserAdapter keyCloakUserAdapter;
    protected static UserContext context;

    @BeforeClass
    public static void setup() throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        logger.info("Begin setup");
        context = new UserContext(getMockContext());
        try {
            keyCloakUserAdapter = KeyCloakUserAdapter.getInstance(context);
        }
        catch(Exception e) {
            logger.error("Exception in setup" + e);
            e.printStackTrace();
            throw e;
        }

        logger.info("finished setup");
    }

    @Test
    public void login(){
        logger.info("Logintest");
        try {
            AccessToken token = keyCloakUserAdapter.login("test1", "test1");

            logger.info("AccessToken is {}", token.toString());

            token = keyCloakUserAdapter.login("test1", "failed");
        }catch(Exception e) {
            logger.error("Exception in login test" + e);
            e.printStackTrace();
        }
    }


}
