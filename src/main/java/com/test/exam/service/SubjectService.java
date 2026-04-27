package com.test.exam.service;

import com.test.exam.dto.SubjectDto;
import com.test.exam.entity.Subject;
import com.test.exam.repository.QuestionRepository;
import com.test.exam.repository.SubjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SubjectService {

    private final SubjectRepository subjectRepository;
    private final QuestionRepository questionRepository;

    public List<SubjectDto> getAll() {
        return subjectRepository.findAll().stream().map(s -> {
            SubjectDto dto = new SubjectDto();
            dto.setId(s.getId());
            dto.setName(s.getName());
            dto.setQuestionCount(questionRepository.findBySubjectId(s.getId()).size());
            return dto;
        }).toList();
    }

    public SubjectDto create(String name) {
        Subject s = new Subject();
        s.setName(name);
        subjectRepository.save(s);
        SubjectDto dto = new SubjectDto();
        dto.setId(s.getId());
        dto.setName(s.getName());
        dto.setQuestionCount(0);
        return dto;
    }

    public void delete(Long id) {
        subjectRepository.deleteById(id);
    }
}
