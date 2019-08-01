/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.pgnig.serwis.bpm.process.child;

import io.restassured.RestAssured;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.web.embedded.undertow.UndertowServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

/**
 *
 * @author jerzy.malyszko
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
public class SamplumTest {

    @Autowired
    private UserDetailsService userDetailService;

    @Test
    public void testRequest() {
        UserDetails defUser = userDetailService.loadUserByUsername("user");
        final String username = defUser.getUsername();
        final String password = defUser.getPassword();
        RestAssured.given().auth().basic(username, password.replace("{noop}", "")).when().get("http://localhost:8079/zupa").then().statusCode(200);
    }

    @TestConfiguration
    static class Config {

        @Bean
        public UndertowServletWebServerFactory servletWebServerFactory() {
            UndertowServletWebServerFactory factory = new UndertowServletWebServerFactory();
            factory.setContextPath("/zupa");
            factory.setPort(8079);
            return factory;
        }

    }

}
