package com.example.judgepaper.controller;

import com.example.judgepaper.Dto.RequestDto.ChangeRequest;
import com.example.judgepaper.service.ChangeScoreService;
import com.example.judgepaper.util.Result;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/change")
public class ChangeScoreController {

    private final ChangeScoreService changeScoreService;

    public ChangeScoreController(ChangeScoreService changeScoreService) {
        this.changeScoreService = changeScoreService;
    }

    @PostMapping(value = "/update")
    public Result<Object> updateScore(@RequestBody ChangeRequest changeRequest){
        boolean b = changeScoreService.updateScoreToDataBase(changeRequest);
        return new Result<>(200,"更改分值成功",b);
    }
}
