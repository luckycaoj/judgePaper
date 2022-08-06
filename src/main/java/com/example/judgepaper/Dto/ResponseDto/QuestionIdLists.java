package com.example.judgepaper.Dto.ResponseDto;

import lombok.Data;

import java.util.List;

@Data
public class QuestionIdLists {
    private List<String> selectIdList;
    private List<String> judgeIdList;
    private List<String> fillIdList;
    private List<String> writeIdList;
}
