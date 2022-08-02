package com.example.judgepaper.Dto.ResponseDto;

import lombok.Data;

import java.util.List;

@Data
public class JudgeAnswerPartDto {

    private List<String> selectResultStatusList;
    private List<String> judgeResultStatusList;
    private List<List<String>> fillResultStatusList;

    private QuestionScore questionScore;

}
