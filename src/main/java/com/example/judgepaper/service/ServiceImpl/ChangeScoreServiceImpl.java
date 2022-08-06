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
     * @param testId
     * @param studentId
     * @return
     */
    @Override
    public StudentScorePart getScoreList(String testId, String studentId) {
        ChangeQuestionList scoreString = changeScoreMapper.getScoreString(studentId, testId);
        String[] split = scoreString.getStudentScore().split("@fg@");
        StudentScorePart studentScorePart = new StudentScorePart();
        List<Integer> selectResultList = new ArrayList<>();
        List<Integer> judgeResultList = new ArrayList<>();
        List<Integer> OneFillResult = new ArrayList<>();
        List<Integer> writeResult = new ArrayList<>();
        //各题型总分
        int selectScore = 0;
        int judgeScore = 0;
        int fillScore = 0;
        int writeScore = 0;
        for (String s : split) {
            if (s.contains("@￥#S@")) {
                //选择题
                String[] selectResult = s.split("@￥#S@");
                for (String s1 : selectResult) {
                    selectResultList.add(Integer.valueOf(s1));
                    selectScore = selectScore + Integer.parseInt(s1);
                }
                studentScorePart.setSelectResultList(selectResultList);
            } else if (s.contains("@￥#J@")) {
                //判断题
                String[] judgeResult = s.split("@￥#J@");
                for (String s1 : judgeResult) {
                    judgeResultList.add(Integer.valueOf(s1));
                    judgeScore = judgeScore + Integer.parseInt(s1);
                }
                studentScorePart.setJudgeResultList(judgeResultList);
            } else if (s.contains("@￥#F@")) {
                //填空题
                String[] fillResultGroup = s.split("@￥#F@");
                for (String s1 : fillResultGroup) {
                    OneFillResult.add(Integer.valueOf(s1));
                    fillScore = fillScore + Integer.parseInt(s1);
                }
                studentScorePart.setFillResultList(OneFillResult);
            } else if (s.contains("@￥#W@")) {
                //简答题
                String[] writeResultGroup = s.split("@￥#W@");
                for (String s1 : writeResultGroup) {
                    writeResult.add(Integer.valueOf(s1));
                    writeScore = writeScore + Integer.parseInt(s1);
                }
                studentScorePart.setWriteResultList(writeResult);
            }
        }
        studentScorePart.setScore(scoreString.getTotalScore());
        studentScorePart.setSelectScore(selectScore);
        studentScorePart.setJudgeScore(judgeScore);
        studentScorePart.setFillScore(fillScore);
        studentScorePart.setWriteScore(writeScore);
        return studentScorePart;
    }

    /**
     * 更改分数后的分数列表
     * @param changeRequest
     * @return
     */
    @Override
    public StudentScorePart updateScoreList(ChangeRequest changeRequest) {
        StudentScorePart scoreList = getScoreList(changeRequest.getTestId(),
                changeRequest.getStudentId());
        //原题目列表
        List<Integer> list = new ArrayList<>();
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
        List<Integer> strings = list.subList(0, selectSize);
        List<Integer> strings1 = list.subList(selectSize, selectSize + judgeSize);
        List<Integer> strings2 = list.subList(selectSize + judgeSize, selectSize + judgeSize + fillSize);
        List<Integer> strings3 = list.subList(selectSize + judgeSize + fillSize, selectSize + judgeSize + fillSize + writeSize);
        scoreList.setSelectResultList(strings);
        scoreList.setJudgeResultList(strings1);
        scoreList.setFillResultList(strings2);
        scoreList.setWriteResultList(strings3);
        return scoreList;
    }

    /**
     * 将更改后的分数列表转为字符串
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
            for (Integer s : studentScorePart.getSelectResultList()) {
                stringBuffer.append(s);
                stringBuffer.append("@￥#S@");
            }
            stringBufferSize = stringBuffer.length();
            stringBuffer.delete(stringBufferSize - 5, stringBufferSize);
            stringBuffer.append("@fg@");
        }
        if (studentScorePart.getJudgeResultList().size() > 0) {
            //判断
            for (Integer s : studentScorePart.getJudgeResultList()) {
                stringBuffer.append(s);
                stringBuffer.append("@￥#J@");
            }
            stringBufferSize = stringBuffer.length();
            stringBuffer.delete(stringBufferSize - 5, stringBufferSize);
            stringBuffer.append("@fg@");
        }
        if (studentScorePart.getFillResultList().size() > 0) {
            //填空
            for (Integer s : studentScorePart.getFillResultList()) {
                stringBuffer.append(s);
                stringBuffer.append("@￥#F@");
            }
            stringBufferSize = stringBuffer.length();
            stringBuffer.delete(stringBufferSize - 5, stringBufferSize);
            stringBuffer.append("@fg@");
        }
        //简答
        if (studentScorePart.getWriteResultList().size() > 0) {
            for (Integer s : studentScorePart.getWriteResultList()) {
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
     * @param changeRequest
     * @return
     */
    @Override
    public boolean updateScoreToDataBase(ChangeRequest changeRequest) {
        StudentScorePart studentScorePart = updateScoreList(changeRequest);
        Double score = studentScorePart.getScore();
        StringBuilder stringBuilder = listToString(changeRequest);
        return changeScoreMapper.updateScore(changeRequest.getStudentId(),
                changeRequest.getTestId(),
                stringBuilder.toString(),
                score);
    }

}
