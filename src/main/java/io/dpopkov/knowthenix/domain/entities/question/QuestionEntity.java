package io.dpopkov.knowthenix.domain.entities.question;

import io.dpopkov.knowthenix.domain.entities.ModifiableEntity;
import io.dpopkov.knowthenix.domain.enums.Language;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@Entity(name = "question")
public class QuestionEntity extends ModifiableEntity {

    @ManyToOne
    private CategoryEntity category;

    @Enumerated(EnumType.STRING)
    private Language selectedLanguage = Language.EN;

    @MapKey(name = "language")
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    private Map<Language, QuestionTextEntity> translations = new HashMap<>();

    public void addTranslation(QuestionTextEntity translation) {
        translations.put(translation.getLanguage(), translation);
    }
}
