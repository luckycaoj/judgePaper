package com.example.judgepaper.service;

import com.example.judgepaper.Dto.ResponseDto.*;

public interface JudgePaperService {

    /**
     * 查找试卷的答案
     * @param id
     * @return PaperAnswerDto
     */
    PaperAnswerDto findAllMassage(String id);

    /**
     * 查找学生的答案
     * @param testId
     * @param paperId
     * @param studentId
     * @return StudentAnswerDto
     */
    StudentAnswerDto findStudentResult(String testId, String paperId, String studentId);

    /**
     * 判卷
     * @param studentAnswerDto
     * @param paperResponseDto
     * @return JudgeAnswerDto
     */
    JudgeAnswerPartDto judgePaper(StudentAnswerDto studentAnswerDto,
                              PaperAnswerDto paperResponseDto);
}
