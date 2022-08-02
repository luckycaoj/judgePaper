package com.example.judgepaper.Dto.ResponseDto;

import lombok.Data;

@Data
public class StudentAnswerDto {

    private String testId;

    private String paperId;

    private String studentId;

    private String questionResult;

    private StudentAnswerPartDto studentAnswerPartDto;

}
