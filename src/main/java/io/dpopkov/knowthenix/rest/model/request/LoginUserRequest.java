package io.dpopkov.knowthenix.rest.model.request;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class LoginUserRequest {

    private String username;
    private String password;
}
