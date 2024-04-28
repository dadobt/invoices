package org.dadobt.casestudy.integrationtest;

import org.dadobt.casestudy.controller.dto.AuthenticationRequest;
import org.dadobt.casestudy.controller.dto.AuthenticationResponse;
import org.dadobt.casestudy.integrationtest.config.IntegrationTest;
import org.dadobt.casestudy.service.MyUserDetailsService;
import org.dadobt.casestudy.util.JwtUtil;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class AuthenticateControllerTest extends IntegrationTest {

    String url;
    private static final String AUTHENTICATE_ENDPOINT = "/authenticate";

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtTokenUtil;

    @Autowired
    private MyUserDetailsService userDetailsService;

    @Before
    public void setUp() {
        url = getBaseUrl();
    }

    @Test
    public void givenAuthenticationNeedsToBeDone_whenAuthenticateEndpointIsCalled_thenUserIsAuthenticatedAndTokenIsGenerated() {
        AuthenticationRequest authenticateRequest = createAuthenticateRequest();
        ResponseEntity<AuthenticationResponse> responseEntity = restTemplate.postForEntity(url + AUTHENTICATE_ENDPOINT, authenticateRequest, AuthenticationResponse.class);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
    }
}