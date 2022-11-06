package io.dpopkov.knowthenix.rest.model.request;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class RegisterUserRequest {

    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String email;
}
