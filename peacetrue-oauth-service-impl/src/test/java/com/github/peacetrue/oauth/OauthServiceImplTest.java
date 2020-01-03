package com.github.peacetrue.oauth;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author xiayx
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestServiceOauthAutoConfiguration.class)
public class OauthServiceImplTest {

    @Autowired
    private OauthService oauthService;

    @Test
    public void generateUrl4GetCode() {
        // snsapi_base snsapi_union_login
        String codeUrl = oauthService.generateAuthorizationUrl("snsapi_base");
        System.out.println(codeUrl);
    }
}