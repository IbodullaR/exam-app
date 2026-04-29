package com.test.exam.dto;

import lombok.Data;

@Data
public class QuestionDto {
    private Long id;
    private Long subjectId;
    private String subjectName;
    private String questionText;
    private String optionA;
    private String optionB;
    private String optionC;
    private String optionD;
    private Integer difficulty;
    private String correctAnswer;
}
