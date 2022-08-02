package com.example.judgepaper.Dto.ResponseDto;

import lombok.Data;

@Data
public class Question {

    private String que_id;

    private String que_type;

    private Integer que_score;

    private String answers;

}
