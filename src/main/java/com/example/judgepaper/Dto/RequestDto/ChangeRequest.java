package com.example.judgepaper.Dto.RequestDto;

import lombok.Data;

@Data
public class ChangeRequest {
    private String testId;
    private String studentId;
    private String questionNum;
    private Integer score;
}
