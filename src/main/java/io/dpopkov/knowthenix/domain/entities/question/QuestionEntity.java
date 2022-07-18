package io.dpopkov.knowthenix.domain.entities.question;

import io.dpopkov.knowthenix.domain.entities.KeyTermEntity;
import io.dpopkov.knowthenix.domain.entities.ModifiableEntity;
import io.dpopkov.knowthenix.domain.enums.Language;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

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

    @ManyToMany
    @JoinTable(name = "question_keyterm",
            joinColumns = @JoinColumn(name = "question_id"),
            inverseJoinColumns = @JoinColumn(name = "keyterm_id"))
    private Set<KeyTermEntity> keyTerms = new HashSet<>();

    public void addTranslation(QuestionTextEntity translation) {
        translations.put(translation.getLanguage(), translation);
    }

    public void addKeyTerm(KeyTermEntity keyTerm) {
        this.keyTerms.add(keyTerm);
    }
}
