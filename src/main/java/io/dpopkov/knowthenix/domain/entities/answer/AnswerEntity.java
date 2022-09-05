package io.dpopkov.knowthenix.domain.entities.answer;

import io.dpopkov.knowthenix.domain.entities.KeyTermEntity;
import io.dpopkov.knowthenix.domain.entities.ModifiableEntity;
import io.dpopkov.knowthenix.domain.entities.question.QuestionEntity;
import io.dpopkov.knowthenix.domain.enums.Language;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

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
    @OneToMany(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private Map<Language, AnswerTextEntity> translations = new HashMap<>();

    @ManyToMany
    @JoinTable(name = "answer_keyterm",
            joinColumns = @JoinColumn(name = "answer_id"),
            inverseJoinColumns = @JoinColumn(name = "keyterm_id"))
    private Set<KeyTermEntity> keyTerms = new HashSet<>();

    public void addTranslation(AnswerTextEntity translation) {
        translations.put(translation.getLanguage(), translation);
    }

    public void addKeyTerm(KeyTermEntity keyTerm) {
        this.keyTerms.add(keyTerm);
    }

    public void removeKeyTerm(KeyTermEntity keyTerm) {
        this.keyTerms.remove(keyTerm);
    }
}
