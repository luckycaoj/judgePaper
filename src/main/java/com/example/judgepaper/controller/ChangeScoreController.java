package com.example.judgepaper.controller;

import com.example.judgepaper.Dto.RequestDto.ChangeRequest;
import com.example.judgepaper.Dto.ResponseDto.Change.StudentScorePart;
import com.example.judgepaper.service.ChangeScoreService;
import com.example.judgepaper.util.Result;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/change")
public class ChangeScoreController {

    private final ChangeScoreService changeScoreService;

    public ChangeScoreController(ChangeScoreService changeScoreService) {
        this.changeScoreService = changeScoreService;
    }

    @GetMapping(value = "/get")
    public Result<Object> getScore(@RequestParam("testid") String testId,
                                   @RequestParam("studentid") String studentId){
        StudentScorePart scoreList = changeScoreService
                .getScoreList(testId, studentId);
        return new Result<>(200,"学生分数信息",scoreList);
    }

    @PostMapping(value = "/update")
    public Result<Object> updateScore(@RequestBody ChangeRequest changeRequest){
        boolean b = changeScoreService.updateScoreToDataBase(changeRequest);
        return new Result<>(200,"更改分值成功",b);
    }
}
