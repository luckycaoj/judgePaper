package com.example.judgepaper.Dto.ResponseDto.Change;

import lombok.Data;

import java.util.List;
@Data
public class StudentScorePart {
    private List<Integer> selectResultList;
    private List<Integer> judgeResultList;
    private List<Integer> fillResultList;
    private List<Integer> writeResultList;
    private Double score;

    private Integer selectScore;
    private Integer judgeScore;
    private Integer fillScore;
    private Integer writeScore;}
