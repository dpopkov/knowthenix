package io.dpopkov.knowthenix.domain.entities.answer;

import io.dpopkov.knowthenix.domain.entities.ModifiableEntity;
import io.dpopkov.knowthenix.domain.enums.SourceType;
import lombok.*;
import org.springframework.lang.Nullable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "source")
public class SourceEntity extends ModifiableEntity {

    @NotEmpty
    @Column(nullable = false, unique = true)
    private String name;
    private String fullTitle;
    private String url;
    @NotNull
    @Enumerated(EnumType.STRING)
    private SourceType sourceType;
    private String description;

    public void copyFrom(@Nullable SourceEntity other) {
        if (other == null) {
            throw new IllegalArgumentException("The source entity is null");
        }
        this.name = other.name;
        this.fullTitle = other.fullTitle;
        this.url = other.url;
        this.sourceType = other.sourceType;
        this.description = other.description;
    }

}
