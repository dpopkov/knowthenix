package io.dpopkov.knowthenix.services.dto.converters;

import io.dpopkov.knowthenix.domain.entities.user.AuthUserEntity;
import io.dpopkov.knowthenix.services.dto.AuthUserDto;
import org.springframework.stereotype.Component;

@Component
public class AuthUserEntityToDto extends BaseConverter<AuthUserEntity, AuthUserDto> {
    @Override
    public AuthUserDto convert(AuthUserEntity entity) {
        return mapper.map(entity, AuthUserDto.class);
    }
}
