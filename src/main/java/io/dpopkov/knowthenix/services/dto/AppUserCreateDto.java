package io.dpopkov.knowthenix.services.dto;

import io.dpopkov.knowthenix.domain.entities.user.AppUserEntity;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
public class AppUserCreateDto extends BaseDto {
    private String name;
    private String password;

    public AppUserEntity asUserEntity() {
        AppUserEntity entity = new AppUserEntity();
        entity.setName(this.name);
        entity.setPassword(this.password);
        return entity;
    }
}
