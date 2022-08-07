package com.example.judgepaper.Dto.ResponseDto;

import lombok.Data;

import java.util.List;

@Data
public class QuestionScore {
    private List<Integer> selectScoreList;
    private List<Integer> judgeScoreList;
    private List<Integer> fillScoreList;
    private List<Integer> writeScoreList;

    private Integer selectScore;
    private Integer judgeScore;
    private Integer fillScore;
    private Integer writeScore;
    private Integer sumScore;
}
