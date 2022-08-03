package com.example.judgepaper.controller;

import com.example.judgepaper.Dto.ResponseDto.JudgeAnswerPartDto;
import com.example.judgepaper.Dto.ResponseDto.PaperAnswerDto;
import com.example.judgepaper.Dto.ResponseDto.StudentAnswerDto;
import com.example.judgepaper.service.JudgePaperService;
import com.example.judgepaper.util.Result;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/judge")
public class JudgePaperController {

    private final JudgePaperService judgePaperService;

    public JudgePaperController(JudgePaperService judgePaperService) {
        this.judgePaperService = judgePaperService;
    }

    @GetMapping(value = "/find")
    public Result<Object> find(@RequestParam("paperid") String id) {
        PaperAnswerDto allMassage = judgePaperService.findAllMassage(id);
        if(allMassage == null){
            return new Result<>(500, "查无此卷", null);
        }else {
            return new Result<>(200, "查询成功", allMassage);
        }
    }

    @GetMapping(value = "/stu")
    public Result<Object> findStu(@RequestParam("testid") String testId,
                                  @RequestParam("paperid") String paperId,
                                  @RequestParam("studentid") String studentId) {
        StudentAnswerDto studentResult = judgePaperService
                .findStudentResult(testId, paperId, studentId);
        return new Result<>(200, "查询成功", studentResult.getStudentAnswerPartDto());
    }

    @GetMapping(value = "/done")
    public Result<Object> judge(@RequestParam("testid") String testId,
                                @RequestParam("paperid") String paperId,
                                @RequestParam("studentid") String studentId) {
        PaperAnswerDto paperAnswerDto = judgePaperService
                .findAllMassage(paperId);
        StudentAnswerDto studentAnswerDto = judgePaperService
                .findStudentResult(testId, paperId, studentId);
        JudgeAnswerPartDto judgeAnswerPartDto = judgePaperService.judgePaper(studentAnswerDto, paperAnswerDto);
        boolean flag = judgePaperService.studentScoreInsert(judgeAnswerPartDto, paperAnswerDto,studentAnswerDto);
        if(flag && judgeAnswerPartDto!=null){
            return new Result<>(200, "查询且插入成功", judgeAnswerPartDto);
        }
        else
            return new Result<>(500,"判卷出错，请检查后端",null);
    }
}
