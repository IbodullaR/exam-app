package com.test.exam.dto;

import lombok.Data;

@Data
public class QuestionCreateDto {
    private Long subjectId;
    private String questionText;
    private String optionA;
    private String optionB;
    private String optionC;
    private String optionD;
    private String correctAnswer;
    private Integer difficulty; // 1=oson, 2=o'rta, 3=qiyin
}
