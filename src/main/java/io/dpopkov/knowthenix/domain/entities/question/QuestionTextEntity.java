package io.dpopkov.knowthenix.domain.entities.question;

import io.dpopkov.knowthenix.domain.entities.AbstractTranslationEntity;
import io.dpopkov.knowthenix.domain.enums.Language;
import io.dpopkov.knowthenix.domain.enums.TextType;

import javax.persistence.Entity;

@Entity(name = "question_text")
public class QuestionTextEntity extends AbstractTranslationEntity {

    public QuestionTextEntity() {
    }

    public QuestionTextEntity(Language language, String text) {
        super(language, text);
    }

    public QuestionTextEntity(Language language, TextType type, String text) {
        super(language, type, text);
    }

    @Override
    public String toString() {
        return "QuestionTextEntity" + super.toString();
    }
}
