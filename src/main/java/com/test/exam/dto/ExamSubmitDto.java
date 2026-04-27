package com.test.exam.dto;

import lombok.Data;
import java.util.Map;

@Data
public class ExamSubmitDto {
    // key: questionId, value: tanlangan javob
    private Map<Long, String> answers;
}
