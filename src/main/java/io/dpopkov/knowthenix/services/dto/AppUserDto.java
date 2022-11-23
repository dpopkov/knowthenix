package io.dpopkov.knowthenix.services.dto;

import io.dpopkov.knowthenix.domain.entities.user.AppUserEntity;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
public class AppUserDto {
    private String publicId;
    private String fullName;

    public static AppUserDto from(AppUserEntity userEntity) {
        AppUserDto user = new AppUserDto();
        user.setPublicId(userEntity.getPublicId());
        user.setFullName(userEntity.getFullName());
        return user;
    }
}
