package io.dpopkov.knowthenix.services.impl;

import io.dpopkov.knowthenix.domain.entities.KeyTermEntity;
import io.dpopkov.knowthenix.domain.entities.answer.AnswerEntity;
import io.dpopkov.knowthenix.domain.entities.answer.AnswerTextEntity;
import io.dpopkov.knowthenix.domain.entities.answer.SourceEntity;
import io.dpopkov.knowthenix.domain.repositories.AnswerRepository;
import io.dpopkov.knowthenix.domain.repositories.AnswerTextRepository;
import io.dpopkov.knowthenix.domain.repositories.KeyTermRepository;
import io.dpopkov.knowthenix.domain.repositories.SourceRepository;
import io.dpopkov.knowthenix.services.AnswerService;
import io.dpopkov.knowthenix.services.AppServiceException;
import io.dpopkov.knowthenix.services.dto.AnswerDto;
import io.dpopkov.knowthenix.services.dto.IdChangeSetDto;
import io.dpopkov.knowthenix.services.dto.KeyTermDto;
import io.dpopkov.knowthenix.services.dto.TranslationDto;
import io.dpopkov.knowthenix.services.dto.converters.AnswerDtoToEntity;
import io.dpopkov.knowthenix.services.dto.converters.AnswerEntityToDto;
import io.dpopkov.knowthenix.services.dto.converters.AnswerTextEntityToDto;
import io.dpopkov.knowthenix.services.dto.converters.TranslationDtoToAnswerTextEntity;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static io.dpopkov.knowthenix.shared.Utils.anyIdIsMissing;

@Service
public class AnswerServiceImpl implements AnswerService {

    private final AnswerRepository answerRepository;
    private final AnswerDtoToEntity answerDtoToEntity;
    private final AnswerEntityToDto answerEntityToDto;
    private final AnswerTextEntityToDto answerTextEntityToDto;
    private final TranslationDtoToAnswerTextEntity translationDtoToAnswerTextEntity;
    private final AnswerTextRepository answerTextRepository;
    private final SourceRepository sourceRepository;
    private final KeyTermRepository keyTermRepository;

    public AnswerServiceImpl(AnswerRepository answerRepository, AnswerDtoToEntity answerDtoToEntity,
                             AnswerEntityToDto answerEntityToDto, AnswerTextEntityToDto answerTextEntityToDto,
                             TranslationDtoToAnswerTextEntity translationDtoToAnswerTextEntity, AnswerTextRepository answerTextRepository, SourceRepository sourceRepository, KeyTermRepository keyTermRepository) {
        this.answerRepository = answerRepository;
        this.answerDtoToEntity = answerDtoToEntity;
        this.answerEntityToDto = answerEntityToDto;
        this.answerTextEntityToDto = answerTextEntityToDto;
        this.translationDtoToAnswerTextEntity = translationDtoToAnswerTextEntity;
        this.answerTextRepository = answerTextRepository;
        this.sourceRepository = sourceRepository;
        this.keyTermRepository = keyTermRepository;
    }

    @Override
    public List<AnswerDto> getAllForQuestion(Long questionId) {
        List<AnswerEntity> allByQuestion = answerRepository.findAllByQuestionId(questionId);
        List<AnswerDto> result = new ArrayList<>();
        allByQuestion.forEach(e -> result.add(answerEntityToDto.convert(e)));
        return result;
    }

    @Override
    public AnswerDto create(AnswerDto dto) {
        if (anyIdIsMissing(dto.getQuestionId(), dto.getSourceId())) {
            throw new AppServiceException(
                    "Some of IDs of associated Question or Source for the created Answer are missing");
        }
        AnswerEntity entity = answerDtoToEntity.convert(dto);
        SourceEntity sourceEntity = sourceRepository.findById(dto.getSourceId()).orElseThrow(
                () -> new AppServiceException("Cannot find Source by ID " + dto.getSourceId())
        );
        entity.setSource(sourceEntity);
        AnswerEntity saved = answerRepository.save(entity);
        AnswerDto converted = answerEntityToDto.convert(saved);
        converted.setSourceName(sourceEntity.getName());
        return converted;
    }

    @Override
    public AnswerDto getById(Long id) {
        AnswerEntity found = answerRepository.findById(id).orElseThrow(
                () -> new AppServiceException("Cannot find Answer by ID " + id));
        return answerEntityToDto.convert(found);
    }

    @Override
    public List<AnswerDto> getAll() {
        return null;
    }

    @Override
    public AnswerDto update(AnswerDto dto) {
        if (anyIdIsMissing(dto.getQuestionId(), dto.getSourceId())) {
            throw new AppServiceException(
                    "Some of IDs of associated Question or Source for the updated Answer are missing");
        }
        AnswerEntity foundEntity = answerRepository.findById(dto.getId()).orElseThrow(
                () -> new AppServiceException("Cannot find Answer by ID " + dto.getId()));
        if (!foundEntity.getSource().getId().equals(dto.getSourceId())) {
            SourceEntity updatedSource = sourceRepository.findById(dto.getSourceId()).orElseThrow(
                    () -> new AppServiceException("Cannot find Source by ID " + dto.getSourceId()));
            foundEntity.setSource(updatedSource);
        }
        AnswerEntity draftEntity = answerDtoToEntity.convert(dto);
        foundEntity.setSourceDetails(draftEntity.getSourceDetails());
        foundEntity.setSelectedLanguage(draftEntity.getSelectedLanguage());
        foundEntity.setTranslations(draftEntity.getTranslations());
        AnswerEntity saved = answerRepository.save(foundEntity);
        return answerEntityToDto.convert(saved);
    }

    @Override
    public void delete(Long id) {

    }

    @Override
    public List<TranslationDto> getTranslations(Long answerId) {
        AnswerEntity answerEntity = answerRepository.findById(answerId).orElseThrow();
        final Collection<AnswerTextEntity> values = answerEntity.getTranslations().values();
        List<TranslationDto> result = new ArrayList<>();
        values.forEach(textEntity -> result.add(answerTextEntityToDto.convert(textEntity)));
        return result;
    }

    @Transactional
    @Override
    public TranslationDto addTranslation(Long answerId, TranslationDto translation) {
        AnswerEntity answer = answerRepository.findById(answerId)
                .orElseThrow(() -> new AppServiceException("Cannot find answer by ID to add translation"));
        AnswerTextEntity textEntity = translationDtoToAnswerTextEntity.convert(translation);
        AnswerTextEntity savedTextEntity = answerTextRepository.save(textEntity);
        answer.addTranslation(savedTextEntity);
        answerRepository.save(answer);
        return answerTextEntityToDto.convert(savedTextEntity);
    }

    @Override
    public TranslationDto updateTranslation(Long answerId, TranslationDto translation) {
        if (!answerRepository.existsById(answerId)) {
            throw new AppServiceException("Cannot find answer by ID to update translation");
        }
        AnswerTextEntity textEntity = answerTextRepository.findById(translation.getId())
                .orElseThrow(() -> new AppServiceException("Cannot find AnswerTextEntity by ID"));
        AnswerTextEntity data = translationDtoToAnswerTextEntity.convert(translation);
        textEntity.copyFrom(data);
        AnswerTextEntity updated = answerTextRepository.save(textEntity);
        return answerTextEntityToDto.convert(updated);
    }

    @Override
    public Collection<KeyTermDto> getKeyTermsByAnswerId(Long answerId) {
        AnswerEntity answerEntity = answerRepository.findById(answerId)
                .orElseThrow(() -> new AppServiceException("Cannot find answer by ID to get keyterms"));
        Collection<KeyTermEntity> entities = answerEntity.getKeyTerms();
        Collection<KeyTermDto> result = new ArrayList<>();
        ModelMapper mapper = new ModelMapper();
        entities.forEach(e -> result.add(mapper.map(e, KeyTermDto.class)));
        return result;
    }

    @Override
    public Collection<Long> changeKeyTermsByAnswerId(Long answerId, IdChangeSetDto idChangeSetDto) {
        AnswerEntity answerEntity = answerRepository.findById(answerId)
                .orElseThrow(() -> new AppServiceException("Cannot find answer by ID to change keyterms"));
        idChangeSetDto.getAdd().forEach(keyTermId -> {
            Optional<KeyTermEntity> byId = keyTermRepository.findById(keyTermId);
            byId.ifPresent(answerEntity::addKeyTerm);
        });
        idChangeSetDto.getRemove().forEach(keyTermId -> {
            Optional<KeyTermEntity> byId = keyTermRepository.findById(keyTermId);
            byId.ifPresent(answerEntity::removeKeyTerm);
        });
        answerRepository.save(answerEntity);
        Collection<Long> result = new ArrayList<>();
        answerEntity.getKeyTerms().forEach(kt -> result.add(kt.getId()));
        return result;
    }
}
