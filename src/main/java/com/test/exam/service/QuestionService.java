package com.test.exam.service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.test.exam.dto.QuestionCreateDto;
import com.test.exam.dto.QuestionDto;
import com.test.exam.entity.Question;
import com.test.exam.entity.Subject;
import com.test.exam.repository.QuestionRepository;
import com.test.exam.repository.SubjectRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class QuestionService {

    private final QuestionRepository questionRepository;
    private final SubjectRepository subjectRepository;

    public List<QuestionDto> getAll() {
        return questionRepository.findAll().stream().map(this::toDto).toList();
    }

    public List<QuestionDto> getBySubject(Long subjectId) {
        return questionRepository.findBySubjectId(subjectId).stream().map(this::toDto).toList();
    }

    public QuestionDto create(QuestionCreateDto dto) {
        Subject subject = subjectRepository.findById(dto.getSubjectId())
                .orElseThrow(() -> new RuntimeException("Subject topilmadi: " + dto.getSubjectId()));
        
        Question q = convertToEntity(dto, subject);
        return toDto(questionRepository.save(q));
    }

    /**
     * Ommaviy saqlash uchun optimallashtirilgan metod
     */
    public List<QuestionDto> createBulk(List<QuestionCreateDto> dtos) {
        // 1. Barcha kerakli subject ID'larini yig'ib olamiz
        Set<Long> subjectIds = dtos.stream()
                .map(QuestionCreateDto::getSubjectId)
                .collect(Collectors.toSet());

        // 2. Barcha fanlarni bitta so'rovda bazadan olamiz (N+1 muammosini oldini oladi)
        Map<Long, Subject> subjectMap = subjectRepository.findAllById(subjectIds).stream()
                .collect(Collectors.toMap(Subject::getId, s -> s));

        // 3. DTO'larni Entity'ga o'giramiz
        List<Question> questions = dtos.stream().map(dto -> {
            Subject subject = subjectMap.get(dto.getSubjectId());
            if (subject == null) {
                throw new RuntimeException("Subject topilmadi: " + dto.getSubjectId());
            }
            return convertToEntity(dto, subject);
        }).toList();

        // 4. Barcha savollarni bitta tranzaksiyada saqlaymiz
        return questionRepository.saveAll(questions).stream()
                .map(this::toDto)
                .toList();
    }

    // Kod takrorlanishini oldini olish uchun yordamchi metod
    private Question convertToEntity(QuestionCreateDto dto, Subject subject) {
        Question q = new Question();
        q.setSubject(subject);
        q.setQuestionText(dto.getQuestionText());
        q.setOptionA(dto.getOptionA());
        q.setOptionB(dto.getOptionB());
        q.setOptionC(dto.getOptionC());
        q.setOptionD(dto.getOptionD());
        q.setCorrectAnswer(dto.getCorrectAnswer());
        q.setDifficulty(dto.getDifficulty());
        return q;
    }

    public void delete(Long id) {
        questionRepository.deleteById(id);
    }

    public QuestionDto toDto(Question q) {
        QuestionDto dto = new QuestionDto();
        dto.setId(q.getId());
        dto.setSubjectId(q.getSubject().getId());
        dto.setSubjectName(q.getSubject().getName());
        dto.setQuestionText(q.getQuestionText());
        dto.setOptionA(q.getOptionA());
        dto.setOptionB(q.getOptionB());
        dto.setOptionC(q.getOptionC());
        dto.setOptionD(q.getOptionD());
        dto.setDifficulty(q.getDifficulty());
        dto.setCorrectAnswer(q.getCorrectAnswer());
        return dto;
    }
}