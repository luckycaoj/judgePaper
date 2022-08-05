package com.example.judgepaper.Dto.ResponseDto.Change;

import lombok.Data;

import java.util.List;
@Data
public class StudentScorePart {
    private List<String> selectResultList;
    private List<String> judgeResultList;
    private List<String> fillResultList;
    private List<String> writeResultList;
}
