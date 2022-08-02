package com.example.judgepaper.Dto.ResponseDto;

import lombok.Data;

@Data
public class QuestionSize {
    private Integer selectSize;
    private Integer fillSize;
    private Integer judgeSize;
    private Integer writeSize;
}
