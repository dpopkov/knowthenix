package io.dpopkov.knowthenix.rest.model.request;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class AddUpdateUserRequest {

    private String currentUsername;
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private String role;
    private Boolean active;
    private Boolean notLocked;
    // todo: add MultipartFile profileImage
}
