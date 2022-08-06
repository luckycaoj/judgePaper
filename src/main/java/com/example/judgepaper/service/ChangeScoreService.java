package com.example.judgepaper.service;

import com.example.judgepaper.Dto.RequestDto.ChangeRequest;
import com.example.judgepaper.Dto.ResponseDto.Change.StudentScorePart;

public interface ChangeScoreService {
    /**
     * 从数据库中拿出字符串，并将其转为列表返回
     * @param testId
     * @param studentId
     * @return
     */
    StudentScorePart getScoreList(String testId, String studentId);

    /**
     * 更改分数后的分数列表
     * @param changeRequest
     * @return
     */
    StudentScorePart updateScoreList(ChangeRequest changeRequest);

    /**
     * 将更改后的分数列表转为字符串
     * @param changeRequest
     * @return
     */
    StringBuilder listToString(ChangeRequest changeRequest);

    /**
     * 将字符串存入数据库
     * @param changeRequest
     * @return
     */
    boolean updateScoreToDataBase(ChangeRequest changeRequest);
}
