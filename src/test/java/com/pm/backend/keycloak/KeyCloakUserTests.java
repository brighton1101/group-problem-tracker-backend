package com.pm.backend.keycloak;



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
        keyCloakUserAdapter = KeyCloakUserAdapter.getInstance(context);

        logger.info("finished setup");
    }

    @Test
    public void login(){
        logger.info("Logintest");
    }


}
