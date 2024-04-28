package org.dadobt.casestudy.init;

import org.dadobt.casestudy.models.ApplicationUser;
import org.dadobt.casestudy.repository.ApplicationUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class InitializeUsers {

    public static final String AGENT = "Agent";

    private ApplicationUserRepository applicationUserRepository;

    private PasswordEncoder passwordEncoder;

    @Autowired
    public InitializeUsers(ApplicationUserRepository applicationUserRepository, PasswordEncoder passwordEncoder) {
        this.applicationUserRepository = applicationUserRepository;
        this.passwordEncoder = passwordEncoder;

        Long id = 1L;
        String username = "user";
        String password = "password";
        String role = AGENT;
        String encodedPassword = passwordEncoder.encode(password);
        applicationUserRepository.save(new ApplicationUser(id,username, encodedPassword,role));

    }
}