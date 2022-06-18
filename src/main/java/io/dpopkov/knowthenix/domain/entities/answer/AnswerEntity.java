package io.dpopkov.knowthenix.domain.entities.answer;

import io.dpopkov.knowthenix.domain.entities.ModifiableEntity;
import io.dpopkov.knowthenix.domain.entities.question.QuestionEntity;
import io.dpopkov.knowthenix.domain.enums.Language;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@Entity(name = "answer")
public class AnswerEntity extends ModifiableEntity {

    @NotNull
    @ManyToOne
    private QuestionEntity question;

    @NotNull
    @ManyToOne
    private SourceEntity source;

    @NotBlank
    private String sourceDetails;

    @Enumerated(EnumType.STRING)
    private Language selectedLanguage = Language.EN;

    @MapKey(name = "language")
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    private Map<Language, AnswerTextEntity> translations = new HashMap<>();

    public void addTranslation(AnswerTextEntity translation) {
        translations.put(translation.getLanguage(), translation);
    }
}
