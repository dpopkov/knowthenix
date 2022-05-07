package io.dpopkov.knowthenix;

import io.dpopkov.knowthenix.domain.entities.question.CategoryEntity;
import io.dpopkov.knowthenix.domain.repositories.CategoryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class DataInitialization {

    private final CategoryRepository categoryRepository;

    public DataInitialization(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void initData() {
        log.debug("initData started");

        CategoryEntity categoryJava = new CategoryEntity("Java", "Java programming language");
        categoryRepository.save(categoryJava);
        CategoryEntity categorySpring = new CategoryEntity("Spring", "Spring framework");
        categoryRepository.save(categorySpring);

        log.debug("initData saved {} categories", categoryRepository.count());

        log.debug("initData finished");
    }
}
