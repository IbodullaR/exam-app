package com.test.exam.controller;

import com.test.exam.dto.ExamResultDto;
import com.test.exam.dto.ExamSubmitDto;
import com.test.exam.dto.QuestionDto;
import com.test.exam.service.ExamService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/exam")
@RequiredArgsConstructor
@Tag(name = "Exam", description = "Imtihon")
public class ExamController {

    private final ExamService examService;

    @GetMapping("/start")
    @Operation(summary = "50 ta random savol olish (imtihon boshlash)")
    public List<QuestionDto> startExam() {
        return examService.startExam();
    }

    @PostMapping("/submit")
    @Operation(summary = "Javoblarni yuborish va natija olish")
    public ExamResultDto submitExam(@RequestBody ExamSubmitDto dto) {
        return examService.submitExam(dto);
    }
}
