package io.dpopkov.knowthenix.services.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TranslationDto extends BaseDto {
    private String language;
    private String type;
    private String text;
}
