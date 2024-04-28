package org.dadobt.casestudy.models;


import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class ApplicationUser {

    @Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NonNull
    private String username;

    @NonNull
    private String password;

    @NonNull
    private String role;


    public ApplicationUser(Long id, String username, String encodedPassword, String role) {
        this.id=id;
        this.username = username;
        this.password = encodedPassword;
        this.role=role;
    }
}