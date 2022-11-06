package io.dpopkov.knowthenix.domain.entities.user;

import io.dpopkov.knowthenix.domain.entities.ModifiableEntity;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

/**
 * Represents User in the authentication context of the application.
 * For functional purposes the {@link AppUserEntity} should be used.
 */
@Entity(name = "auth_user")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthUserEntity extends ModifiableEntity {

    @Column(nullable = false, unique = true)
    private String publicId;
    private String firstName;
    private String lastName;
    @Column(nullable = false)
    @NotBlank(message ="username cannot be blank.")
    private String username;
    @Column(nullable = false)
    private String encryptedPassword;
    @Column(nullable = false)
    @NotBlank(message ="email cannot be blank.")
    private String email;
    private String profileImageUrl;
    @Column(nullable = false)
    private Date joinDate;
    private Date lastLoginDate;
    private Date lastLoginDateDisplay;
    @Enumerated(EnumType.STRING)
    private Role role;
    @ElementCollection
    private Collection<String> authorities = new ArrayList<>();
    private boolean active;
    private boolean notLocked;
    /** When user is "deleted" it is archived actually. */
    private boolean archived;

    public boolean isNotSameById(AuthUserEntity other) {
        return other == null || getId() == null || !getId().equals(other.getId());
    }
}
