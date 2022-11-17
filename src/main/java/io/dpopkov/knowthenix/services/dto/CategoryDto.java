package io.dpopkov.knowthenix.services.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
public class CategoryDto extends BaseDto {
    private String name;
    private String description;
    private String createdBy;

    public CategoryDto(String name, String description) {
        this.name = name;
        this.description = description;
    }
}
