package io.dpopkov.knowthenix.domain.entities.answer;

import io.dpopkov.knowthenix.domain.entities.AbstractTranslationEntity;
import io.dpopkov.knowthenix.domain.enums.Language;
import io.dpopkov.knowthenix.domain.enums.TextType;

import javax.persistence.Entity;

@Entity(name = "answer_text")
public class AnswerTextEntity extends AbstractTranslationEntity {

    public AnswerTextEntity() {
    }

    public AnswerTextEntity(Language language, String text) {
        super(language, text);
    }

    public AnswerTextEntity(Language language, TextType type, String text) {
        super(language, type, text);
    }

    @Override
    public String toString() {
        return "AnswerTextEntity" + super.toString();
    }
}
