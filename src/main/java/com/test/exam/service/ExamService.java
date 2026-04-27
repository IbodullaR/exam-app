package com.test.exam.service;

import com.test.exam.dto.ExamResultDto;
import com.test.exam.dto.ExamSubmitDto;
import com.test.exam.dto.QuestionDto;
import com.test.exam.entity.Question;
import com.test.exam.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
public class ExamService {

    private static final int EXAM_SIZE = 50;
    private final QuestionRepository questionRepository;
    private final QuestionService questionService;

    public List<QuestionDto> startExam() {
        List<Question> all = questionRepository.findAll();
        int size = Math.min(EXAM_SIZE, all.size());
        List<Question> random = questionRepository.findRandomQuestions(size);
        return random.stream().map(questionService::toDto).toList();
    }

    public ExamResultDto submitExam(ExamSubmitDto submitDto) {
        Map<Long, String> answers = submitDto.getAnswers();
        List<Question> questions = questionRepository.findAllById(answers.keySet());

        int correct = 0;
        for (Question q : questions) {
            String given = answers.get(q.getId());
            if (q.getCorrectAnswer().equals(given)) correct++;
        }

        int total = questions.size();
        double pct = total > 0 ? (correct * 100.0 / total) : 0;

        ExamResultDto result = new ExamResultDto();
        result.setTotalQuestions(total);
        result.setCorrectAnswers(correct);
        result.setWrongAnswers(total - correct);
        result.setPercentage(Math.round(pct * 10.0) / 10.0);

        if (pct >= 90) { result.setGrade(5); result.setGradeText("A'lo"); result.setPassed(true); }
        else if (pct >= 75) { result.setGrade(4); result.setGradeText("Yaxshi"); result.setPassed(true); }
        else if (pct >= 55) { result.setGrade(3); result.setGradeText("Qoniqarli"); result.setPassed(true); }
        else { result.setGrade(2); result.setGradeText("Qoniqarsiz"); result.setPassed(false); }

        return result;
    }
}
