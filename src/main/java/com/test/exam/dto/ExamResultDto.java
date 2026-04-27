package com.test.exam.dto;

import lombok.Data;

@Data
public class ExamResultDto {
    private int totalQuestions;
    private int correctAnswers;
    private int wrongAnswers;
    private double percentage;
    private int grade;        // 2, 3, 4, 5
    private String gradeText; // "A'lo", "Yaxshi", "Qoniqarli", "Qoniqarsiz"
    private boolean passed;
}
