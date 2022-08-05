package com.example.judgepaper.service.ServiceImpl;

import com.example.judgepaper.Dto.RequestDto.ChangeRequest;
import com.example.judgepaper.Dto.ResponseDto.Change.ChangeQuestionList;
import com.example.judgepaper.Dto.ResponseDto.Change.StudentScorePart;
import com.example.judgepaper.mapper.ChangeScoreMapper;
import com.example.judgepaper.service.ChangeScoreService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
@Service
@Transactional
public class ChangeScoreServiceImpl implements ChangeScoreService {

    private final ChangeScoreMapper changeScoreMapper;

    public ChangeScoreServiceImpl(ChangeScoreMapper changeScoreMapper) {
        this.changeScoreMapper = changeScoreMapper;
    }

    /**
     * 从数据库中拿出字符串，并将其转为列表返回
     *
     * @param testId
     * @param studentId
     * @return
     */
    @Override
    public StudentScorePart getScoreList(String testId, String studentId) {
        ChangeQuestionList scoreString = changeScoreMapper.getScoreString(studentId, testId);
        String[] split = scoreString.getStudentScore().split("@fg@");
        StudentScorePart studentScorePart = new StudentScorePart();
        List<String> selectResultList = new ArrayList<>();
        List<String> judgeResultList = new ArrayList<>();
        List<String> OneFillResult = new ArrayList<>();
        List<String> writeResult = new ArrayList<>();
        for (String s : split) {
            if (s.contains("@￥#S@")) {
                //选择题
                String[] selectResult = s.split("@￥#S@");
                Collections.addAll(selectResultList, selectResult);
                studentScorePart.setSelectResultList(selectResultList);
            } else if (s.contains("@￥#J@")) {
                //判断题
                String[] judgeResult = s.split("@￥#J@");
                Collections.addAll(judgeResultList, judgeResult);
                studentScorePart.setJudgeResultList(judgeResultList);
            } else if (s.contains("@￥#F@")) {
                //填空题
                String[] fillResultGroup = s.split("@￥#F@");
                Collections.addAll(OneFillResult, fillResultGroup);
                studentScorePart.setFillResultList(OneFillResult);
            } else if (s.contains("@￥#W@")) {
                //填空题
                String[] fillResultGroup = s.split("@￥#W@");
                Collections.addAll(writeResult, fillResultGroup);
                studentScorePart.setWriteResultList(writeResult);
            }
        }
        return studentScorePart;
    }

    /**
     * 更改分数后的分数列表
     *
     * @param changeRequest
     * @return
     */
    @Override
    public StudentScorePart updateScoreList(ChangeRequest changeRequest) {
        StudentScorePart scoreList = getScoreList(changeRequest.getTestId(),
                changeRequest.getStudentId());
        //原题目列表
        List<String> list = new ArrayList<>();
        list.addAll(scoreList.getSelectResultList());
        list.addAll(scoreList.getJudgeResultList());
        list.addAll(scoreList.getFillResultList());
        list.addAll(scoreList.getWriteResultList());
        int selectSize = scoreList.getSelectResultList().size();
        int judgeSize = scoreList.getJudgeResultList().size();
        int fillSize = scoreList.getFillResultList().size();
        int writeSize = scoreList.getWriteResultList().size();
        //修改某题的分值
        list.set(Integer.parseInt(changeRequest.getQuestionNum()) - 1, changeRequest.getScore());
        scoreList.setSelectResultList(list.subList(0, selectSize));
        scoreList.setJudgeResultList(list.subList(selectSize, selectSize + judgeSize));
        scoreList.setFillResultList(list.subList(selectSize + judgeSize, selectSize + judgeSize + fillSize));
        scoreList.setWriteResultList(list.subList(selectSize + judgeSize + fillSize, selectSize + judgeSize + fillSize + writeSize));
        return scoreList;
    }

    /**
     * 将更改后的分数列表转为字符串
     *
     * @param changeRequest
     * @return
     */
    @Override
    public StringBuilder listToString(ChangeRequest changeRequest) {
        StudentScorePart studentScorePart = updateScoreList(changeRequest);
        StringBuilder stringBuffer = new StringBuilder();
        int stringBufferSize;
        if (studentScorePart.getSelectResultList().size() > 0) {
            //选择
            for (String s : studentScorePart.getSelectResultList()) {
                stringBuffer.append(s);
                stringBuffer.append("@￥#S@");
            }
            stringBufferSize = stringBuffer.length();
            stringBuffer.delete(stringBufferSize - 5, stringBufferSize);
            stringBuffer.append("@fg@");
        }
        if (studentScorePart.getJudgeResultList().size() > 0) {
            //判断
            for (String s : studentScorePart.getJudgeResultList()) {
                stringBuffer.append(s);
                stringBuffer.append("@￥#J@");
            }
            stringBufferSize = stringBuffer.length();
            stringBuffer.delete(stringBufferSize - 5, stringBufferSize);
            stringBuffer.append("@fg@");
        }
        if (studentScorePart.getFillResultList().size() > 0) {
            //填空
            for (String s : studentScorePart.getFillResultList()) {
                stringBuffer.append(s);
                stringBuffer.append("@￥#F@");
            }
            stringBufferSize = stringBuffer.length();
            stringBuffer.delete(stringBufferSize - 5, stringBufferSize);
            stringBuffer.append("@fg@");
        }
        //简答
        if (studentScorePart.getWriteResultList().size() > 0) {
            for (String s : studentScorePart.getWriteResultList()) {
                stringBuffer.append(s);
                stringBuffer.append("@￥#W@");
            }
            stringBufferSize = stringBuffer.length();
            stringBuffer.delete(stringBufferSize - 5, stringBufferSize);
        }
        return stringBuffer;
    }

    /**
     * 将字符串存入数据库
     *
     * @param changeRequest
     * @return
     */
    @Override
    public boolean updateScoreToDataBase(ChangeRequest changeRequest) {
        StringBuilder stringBuilder = listToString(changeRequest);
        return changeScoreMapper.updateScore(changeRequest.getStudentId(),
                changeRequest.getTestId(),
                stringBuilder.toString());
    }
}
