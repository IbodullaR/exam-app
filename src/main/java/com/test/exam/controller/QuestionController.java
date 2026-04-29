package com.test.exam.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.test.exam.dto.QuestionCreateDto;
import com.test.exam.dto.QuestionDto;
import com.test.exam.service.QuestionService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

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

    @PostMapping("/bulk")
    @Operation(summary = "Ko'plab savollarni bir vaqtda qo'shish")
    public List<QuestionDto> createBulk(@RequestBody List<QuestionCreateDto> dtos) {
        return dtos.stream().map(questionService::create).toList();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Savolni o'chirish")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        questionService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
