package com.example.judgepaper.Dto.ResponseDto;

import lombok.Data;

import java.util.List;

@Data
public class PaperAnswerPartDto {

    private List<String> selectResultList;
    private List<String> judgeResultList;
    private List<List<String>> fillResultList;

    private QuestionSize questionSize;
    private QuestionScore questionScore;
}
