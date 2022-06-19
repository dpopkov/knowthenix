package io.dpopkov.knowthenix;

import io.dpopkov.knowthenix.domain.entities.KeyTermEntity;
import io.dpopkov.knowthenix.domain.entities.answer.AnswerEntity;
import io.dpopkov.knowthenix.domain.entities.answer.AnswerTextEntity;
import io.dpopkov.knowthenix.domain.entities.answer.SourceEntity;
import io.dpopkov.knowthenix.domain.entities.question.CategoryEntity;
import io.dpopkov.knowthenix.domain.entities.question.QuestionEntity;
import io.dpopkov.knowthenix.domain.entities.question.QuestionTextEntity;
import io.dpopkov.knowthenix.domain.enums.Language;
import io.dpopkov.knowthenix.domain.enums.SourceType;
import io.dpopkov.knowthenix.domain.repositories.*;
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
    private final SourceRepository sourceRepository;
    private final AnswerRepository answerRepository;

    public DataInitialization(CategoryRepository categoryRepository, KeyTermRepository keyTermRepository,
                              QuestionRepository questionRepository, SourceRepository sourceRepository,
                              AnswerRepository answerRepository) {
        this.categoryRepository = categoryRepository;
        this.keyTermRepository = keyTermRepository;
        this.questionRepository = questionRepository;
        this.sourceRepository = sourceRepository;
        this.answerRepository = answerRepository;
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

        SourceEntity coreJava = SourceEntity.builder().name("Core Java").fullTitle("Core Java, 11th Edition")
                .url("www.core.org").sourceType(SourceType.BOOK).description("Core Java by Cay Horstmann").build();
        SourceEntity springInAction = SourceEntity.builder().name("Spring in Action").fullTitle("Spring in Action, 5th Edition")
                .url("www.spring.org").sourceType(SourceType.BOOK).description("Spring in Action by Craig Walls").build();
        SourceEntity jdk = SourceEntity.builder().name("JDK API").fullTitle("JDK API documentation")
                .url("www.jdk.org").sourceType(SourceType.API_DOC).build();
        sourceRepository.save(coreJava);
        sourceRepository.save(springInAction);
        sourceRepository.save(jdk);

        log.debug("initData saved {} sources", sourceRepository.count());

        AnswerEntity springAnswer = new AnswerEntity();
        springAnswer.setQuestion(spring);
        springAnswer.setSource(springInAction);
        springAnswer.setSourceDetails("ch.1.1 p4");
        springAnswer.setSelectedLanguage(Language.EN);
        springAnswer.addTranslation(new AnswerTextEntity(Language.EN,
                "Spring is the framework that will help you achieve your goals"));
        springAnswer.addTranslation(new AnswerTextEntity(Language.RU,
                "Spring это фреймворк, который поможет вам достичь ваших целей"));
        answerRepository.save(springAnswer);

        AnswerEntity springAnswer2 = new AnswerEntity();
        springAnswer2.setQuestion(spring);
        springAnswer2.setSource(coreJava);
        springAnswer2.setSourceDetails("ch.11.6.1 p691");
        springAnswer2.setSelectedLanguage(Language.EN);
        springAnswer2.addTranslation(new AnswerTextEntity(Language.EN,
                "Spring is the layout in Swing UI library"));
        springAnswer2.addTranslation(new AnswerTextEntity(Language.RU,
                "Spring это компонент для размещения контролов при программировании Swing приложений"));
        answerRepository.save(springAnswer2);

        log.debug("initData saved {} answers", answerRepository.count());

        log.debug("initData finished");
    }
}
