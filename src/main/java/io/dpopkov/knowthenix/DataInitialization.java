package io.dpopkov.knowthenix;

import io.dpopkov.knowthenix.domain.entities.KeyTermEntity;
import io.dpopkov.knowthenix.domain.entities.question.CategoryEntity;
import io.dpopkov.knowthenix.domain.repositories.CategoryRepository;
import io.dpopkov.knowthenix.domain.repositories.KeyTermRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class DataInitialization {

    private final CategoryRepository categoryRepository;
    private final KeyTermRepository keyTermRepository;

    public DataInitialization(CategoryRepository categoryRepository, KeyTermRepository keyTermRepository) {
        this.categoryRepository = categoryRepository;
        this.keyTermRepository = keyTermRepository;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void initData() {
        log.debug("initData started");

        CategoryEntity categoryJava = new CategoryEntity("Java", "Java programming language");
        categoryRepository.save(categoryJava);
        CategoryEntity categorySpring = new CategoryEntity("Spring", "Spring framework");
        categoryRepository.save(categorySpring);

        log.debug("initData saved {} categories", categoryRepository.count());

        KeyTermEntity keyTermSpring5 = new KeyTermEntity("Spring 5", "Spring framework version 5");
        keyTermRepository.save(keyTermSpring5);
        KeyTermEntity keyTermSpring6 = new KeyTermEntity("Spring 6", "Spring framework version 6");
        keyTermRepository.save(keyTermSpring6);

        log.debug("initData saved {} key terms", keyTermRepository.count());

        log.debug("initData finished");
    }
}
