package io.dpopkov.knowthenix.services.dto;

import io.dpopkov.knowthenix.domain.entities.user.AppUserEntity;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
public class AppUserDto extends BaseDto {
    private String name;

    public static AppUserDto from(AppUserEntity userEntity) {
        AppUserDto user = new AppUserDto();
        user.setId(userEntity.getId());
        user.setName(userEntity.getName());
        return user;
    }
}
