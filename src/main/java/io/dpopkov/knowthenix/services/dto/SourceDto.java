package io.dpopkov.knowthenix.services.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
public class SourceDto extends BaseDto {
    private String name;
    private String fullTitle;
    private String url;
    private String sourceType;
    private String description;
}
