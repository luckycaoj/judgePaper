package com.example.judgepaper.service;

import com.example.judgepaper.Dto.RequestDto.ChangeRequest;
import com.example.judgepaper.Dto.ResponseDto.Change.StudentScorePart;

public interface ChangeScoreService {
    StudentScorePart getScoreList(String testId, String studentId);

    StudentScorePart updateScoreList(ChangeRequest changeRequest);

    StringBuilder listToString(ChangeRequest changeRequest);

    boolean updateScoreToDataBase(ChangeRequest changeRequest);
}
