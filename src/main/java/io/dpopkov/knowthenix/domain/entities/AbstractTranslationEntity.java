package io.dpopkov.knowthenix.domain.entities;

import io.dpopkov.knowthenix.domain.enums.Language;
import io.dpopkov.knowthenix.domain.enums.TextType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@MappedSuperclass
public abstract class AbstractTranslationEntity extends ModifiableEntity {

    @NotNull
    @Column(name = "TR_LANGUAGE", nullable = false)
    @Enumerated(EnumType.STRING)
    private Language language;
    @NotNull
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TextType type;
    @NotEmpty
    @Column(nullable = false)
    @Lob
    private String text;

    // @ManyToOne
    // private AppUserEntity creator;
    // @ManyToOne
    // private AppUserEntity editor;

    public AbstractTranslationEntity() {
    }

    public AbstractTranslationEntity(Language language, String text) {
        this(language, TextType.PLAINTEXT, text);
    }

    public AbstractTranslationEntity(Language language, TextType type, String text) {
        this.language = language;
        this.type = type;
        this.text = text;
    }

    @Override
    public String toString() {
        return "{" +
                "language=" + language +
                ", type=" + type +
                ", text='" + text + '\'' +
                '}' + super.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AbstractTranslationEntity)) return false;
        AbstractTranslationEntity that = (AbstractTranslationEntity) o;
        if (getLanguage() != that.getLanguage()) return false;
        if (getType() != that.getType()) return false;
        return getText().equals(that.getText());
    }

    @Override
    public int hashCode() {
        int result = getLanguage().hashCode();
        result = 31 * result + getType().hashCode();
        result = 31 * result + getText().hashCode();
        return result;
    }
}
