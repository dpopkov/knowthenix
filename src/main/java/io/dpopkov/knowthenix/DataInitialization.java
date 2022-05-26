package io.dpopkov.knowthenix;

import io.dpopkov.knowthenix.domain.entities.KeyTermEntity;
import io.dpopkov.knowthenix.domain.entities.question.CategoryEntity;
import io.dpopkov.knowthenix.domain.entities.question.QuestionEntity;
import io.dpopkov.knowthenix.domain.entities.question.QuestionTextEntity;
import io.dpopkov.knowthenix.domain.enums.Language;
import io.dpopkov.knowthenix.domain.repositories.CategoryRepository;
import io.dpopkov.knowthenix.domain.repositories.KeyTermRepository;
import io.dpopkov.knowthenix.domain.repositories.QuestionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class DataInitialization {

    private final CategoryRepository categoryRepository;
    private final KeyTermRepository keyTermRepository;
    private final QuestionRepository questionRepository;

    public DataInitialization(CategoryRepository categoryRepository, KeyTermRepository keyTermRepository, QuestionRepository questionRepository) {
        this.categoryRepository = categoryRepository;
        this.keyTermRepository = keyTermRepository;
        this.questionRepository = questionRepository;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void initData() {
        log.debug("initData started");

        CategoryEntity categoryJava = new CategoryEntity("Java", "Java programming language");
        categoryRepository.save(categoryJava);
        CategoryEntity categorySpring = new CategoryEntity("Spring", "Spring framework");
        categoryRepository.save(categorySpring);
        CategoryEntity categoryOrm = new CategoryEntity("ORM", "Object-Relational Mapping");
        categoryRepository.save(categoryOrm);

        log.debug("initData saved {} categories", categoryRepository.count());

        KeyTermEntity keyTermSpring5 = new KeyTermEntity("Spring 5", "Spring framework version 5");
        keyTermRepository.save(keyTermSpring5);
        KeyTermEntity keyTermSpring6 = new KeyTermEntity("Spring 6", "Spring framework version 6");
        keyTermRepository.save(keyTermSpring6);

        log.debug("initData saved {} key terms", keyTermRepository.count());

        QuestionEntity spring = new QuestionEntity();
        QuestionTextEntity springEn = new QuestionTextEntity(Language.EN, "What is Spring?");
        QuestionTextEntity springRu = new QuestionTextEntity(Language.RU, "Что такое Spring?");
        spring.addTranslation(springEn);
        spring.addTranslation(springRu);
        spring.setCategory(categorySpring);
        questionRepository.save(spring);
        QuestionEntity hibernate = new QuestionEntity();
        QuestionTextEntity hibernateEn = new QuestionTextEntity(Language.EN, "What is Hibernate?");
        QuestionTextEntity hibernateRu = new QuestionTextEntity(Language.RU, "Что такое Hibernate?");
        hibernate.addTranslation(hibernateEn);
        hibernate.addTranslation(hibernateRu);
        hibernate.setCategory(categoryOrm);
        questionRepository.save(hibernate);

        log.debug("initData saved {} questions", questionRepository.count());

        log.debug("initData finished");
    }
}
