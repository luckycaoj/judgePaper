package com.example.judgepaper.Dto.ResponseDto;

import lombok.Data;

import java.util.List;

@Data
public class StudentAnswerPartDto {

    private List<String> selectResultList;
    private List<String> judgeResultList;
    private List<List<String>> fillResultList;

}
