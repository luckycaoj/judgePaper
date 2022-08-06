package com.example.judgepaper.Dto.ResponseDto;

import lombok.Data;

@Data
public class TwoData {
    boolean flag;
    private StringBuilder stringBuilder;
    private JudgeAnswerPartDto judgeAnswerPartDto;
}
