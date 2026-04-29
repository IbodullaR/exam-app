package com.test.exam.service;

import com.test.exam.dto.QuestionCreateDto;
import com.test.exam.dto.QuestionDto;
import com.test.exam.entity.Question;
import com.test.exam.entity.Subject;
import com.test.exam.repository.QuestionRepository;
import com.test.exam.repository.SubjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
        Question q = new Question();
        q.setSubject(subject);
        q.setQuestionText(dto.getQuestionText());
        q.setOptionA(dto.getOptionA());
        q.setOptionB(dto.getOptionB());
        q.setOptionC(dto.getOptionC());
        q.setOptionD(dto.getOptionD());
        q.setCorrectAnswer(dto.getCorrectAnswer());
        q.setDifficulty(dto.getDifficulty());
        return toDto(questionRepository.save(q));
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
