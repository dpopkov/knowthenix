package io.dpopkov.knowthenix.services.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Collection;
import java.util.Date;

@Getter
@Setter
public class AuthUserDto {

    private String publicId;
    private String firstName;
    private String lastName;
    private String username;
    private String email;
    /** Absolute URL to user profile image. */
    private String profileImageUrl;
    private Date joinDate;
    private Date lastLoginDateDisplay;
    private String role;
    private Collection<String> authorities;
    private String active;
    private String notLocked;
}
