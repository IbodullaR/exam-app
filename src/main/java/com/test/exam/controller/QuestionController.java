package com.test.exam.controller;

import com.test.exam.dto.QuestionCreateDto;
import com.test.exam.dto.QuestionDto;
import com.test.exam.service.QuestionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/questions")
@RequiredArgsConstructor
@Tag(name = "Questions", description = "Savollarni boshqarish")
public class QuestionController {

    private final QuestionService questionService;

    @GetMapping
    @Operation(summary = "Barcha savollarni olish")
    public List<QuestionDto> getAll() {
        return questionService.getAll();
    }

    @GetMapping("/by-subject/{subjectId}")
    @Operation(summary = "Fan bo'yicha savollarni olish")
    public List<QuestionDto> getBySubject(@PathVariable Long subjectId) {
        return questionService.getBySubject(subjectId);
    }

    @PostMapping
    @Operation(summary = "Yangi savol qo'shish")
    public QuestionDto create(@RequestBody QuestionCreateDto dto) {
        return questionService.create(dto);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Savolni o'chirish")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        questionService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
