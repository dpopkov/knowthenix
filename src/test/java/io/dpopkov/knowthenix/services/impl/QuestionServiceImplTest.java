package io.dpopkov.knowthenix.services.impl;

import io.dpopkov.knowthenix.domain.entities.question.QuestionEntity;
import io.dpopkov.knowthenix.domain.repositories.QuestionRepository;
import io.dpopkov.knowthenix.services.dto.QuestionDto;
import io.dpopkov.knowthenix.services.dto.converters.CategoryEntityToDto;
import io.dpopkov.knowthenix.services.dto.converters.QuestionEntityToDto;
import io.dpopkov.knowthenix.services.dto.converters.QuestionTextEntityToDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class QuestionServiceImplTest {

    private static final Long ID_1 = 10L;
    private static final Long ID_2 = 11L;

    @Mock
    QuestionRepository questionRepository;
    QuestionServiceImpl service;

    @BeforeEach
    void setupService() {
        service = new QuestionServiceImpl(questionRepository,
                new QuestionEntityToDto(new CategoryEntityToDto(), new QuestionTextEntityToDto()));
    }

    @Test
    void getAll() {
        // Given
        final QuestionEntity e1 = new QuestionEntity();
        e1.setId(ID_1);
        final QuestionEntity e2 = new QuestionEntity();
        e2.setId(ID_2);
        given(questionRepository.findAll()).willReturn(List.of(e1, e2));
        // When
        List<QuestionDto> all = service.getAll();
        // Then
        assertEquals(2, all.size());
        assertEquals(ID_1, all.get(0).getId());
        assertEquals(ID_2, all.get(1).getId());
        then(questionRepository).should().findAll();
    }
}
