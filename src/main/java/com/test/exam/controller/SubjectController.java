package com.test.exam.controller;

import com.test.exam.dto.SubjectDto;
import com.test.exam.service.SubjectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/subjects")
@RequiredArgsConstructor
@Tag(name = "Subjects", description = "Fanlarni boshqarish")
public class SubjectController {

    private final SubjectService subjectService;

    @GetMapping
    @Operation(summary = "Barcha fanlarni olish")
    public List<SubjectDto> getAll() {
        return subjectService.getAll();
    }

    @PostMapping
    @Operation(summary = "Yangi fan qo'shish")
    public SubjectDto create(@RequestParam String name) {
        return subjectService.create(name);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Fanni o'chirish")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        subjectService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
