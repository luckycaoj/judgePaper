package com.example.judgepaper.service.ServiceImpl;

import com.example.judgepaper.Dto.ResponseDto.*;
import com.example.judgepaper.mapper.ChangeScoreMapper;
import com.example.judgepaper.mapper.JudgePaperMapper;
import com.example.judgepaper.service.JudgePaperService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Slf4j
@Service
@Transactional
public class JudgePaperServiceImpl implements JudgePaperService {

    private final JudgePaperMapper judgePaperMapper;
    private final ChangeScoreMapper changeScoreMapper;

    public JudgePaperServiceImpl(JudgePaperMapper judgePaperMapper, ChangeScoreMapper changeScoreMapper) {
        this.judgePaperMapper = judgePaperMapper;
        this.changeScoreMapper = changeScoreMapper;
    }

    /**
     * 业务逻辑
     * 1.1 在paper中查找试卷
     * 1.2 在paper_question中找试卷对应的题目
     */
    @Override
    public PaperAnswerDto findAllMassage(String id) {
        Integer rightFlag = 1;
        //查询出试卷的所有正确答案，此时题目列表存在questionByPaperId中
        List<Question> questionByPaperId = judgePaperMapper.findQuestionByPaperId(id, rightFlag);
        if (questionByPaperId.size() == 0) {
            return null;
        } else {
            //设置返回值-->PaperAnswerDto
            PaperAnswerDto paperAnswerDto = new PaperAnswerDto();
            //试卷题目数量
            QuestionSize questionSize = new QuestionSize();
            //试卷分数
            QuestionScore questionScore = new QuestionScore();
            //各题型总分
            int selectScore = 0;
            int judgeScore = 0;
            int fillScore = 0;
            int writeScore = 0;
            /* 对答案各题型进行分类
             1.1 分别创建PaperAnswerPartDto中的字符串列表和字符串列表列表的接收对象
             1.2 在循环遍历中依次在字符串列表或字符串列表列表中追加答案
            */
            PaperAnswerPartDto paperAnswerPartDto = new PaperAnswerPartDto();
            List<String> selectResultList = new ArrayList<>();
            List<String> judgeResultList = new ArrayList<>();
            List<List<String>> OneFillResult = new ArrayList<>();
            //分数列表
            List<Integer> selectScoreList = new ArrayList<>();
            List<Integer> judgeScoreList = new ArrayList<>();
            List<Integer> OneFillScoreList = new ArrayList<>();
            List<Integer> writeScoreList = new ArrayList<>();
            //判断题型，对列表进行三次遍历，对不同题型进行答案提取
            for (Question question : questionByPaperId) {
                //截取标签中的数据
                int indexStart = question.getAnswers().indexOf("<p>") + 3;
                int indexEnd = question.getAnswers().indexOf("</p>");
                switch (question.getQue_type()) {
                    case "选择题":
                        //2.1 提取出选择题的正确选项
                        selectScoreList.add(question.getQue_score());
                        selectScore = selectScore + question.getQue_score();
                        String selectAnswer = question.getAnswers().substring(indexStart, indexStart + 1);
                        selectResultList.add(selectAnswer);
                        break;
                    case "填空题":
                        //2.2 提取出填空题的答案
                        OneFillScoreList.add(question.getQue_score());
                        fillScore = fillScore + question.getQue_score();
                        String fillAnswerString = question.getAnswers().substring(indexStart, indexEnd);
                        String[] fillAnswerGroup = fillAnswerString.split("&nbsp");
                        // 2.2.1 每次搜到填空题均要创建一个List用来存储一道填空题的答案
                        List<String> StringList = new ArrayList<>();
                        // 2.2.2 字符串数组转成字符串列表
                        Collections.addAll(StringList, fillAnswerGroup);
                        OneFillResult.add(StringList);
                        break;
                    case "判断题":
                        //1.3 提取判断题的答案
                        judgeScoreList.add(question.getQue_score());
                        judgeScore = judgeScore + question.getQue_score();
                        String judgeAnswer = question.getAnswers().substring(indexStart, indexEnd);
                        if (judgeAnswer.equals("√")) {
                            judgeResultList.add("T");
                        } else if (judgeAnswer.equals("x") || judgeAnswer.equals("X") || judgeAnswer.equals("×")) {
                            judgeResultList.add("F");
                        } else
                            judgeResultList.add(judgeAnswer);
                        break;
                    case "简答题":
                        //1.4 简答题的个数和分值
                        writeScoreList.add(question.getQue_score());
                        writeScore = writeScore + question.getQue_score();
                }
            }
            //返回试卷的正确答案列表
            paperAnswerPartDto.setSelectResultList(selectResultList);
            paperAnswerPartDto.setJudgeResultList(judgeResultList);
            paperAnswerPartDto.setFillResultList(OneFillResult);
            //返回各题型的数量
            questionSize.setSelectSize(selectResultList.size());
            questionSize.setJudgeSize(judgeResultList.size());
            questionSize.setFillSize(OneFillResult.size());
            questionSize.setWriteSize(writeScoreList.size());
            //返回试卷的分数列表
            questionScore.setJudgeScoreList(judgeScoreList);
            questionScore.setSelectScoreList(selectScoreList);
            questionScore.setFillScoreList(OneFillScoreList);
            questionScore.setWriteScoreList(writeScoreList);
            //各题型总分值
            questionScore.setSelectScore(selectScore);
            questionScore.setFillScore(fillScore);
            questionScore.setJudgeScore(judgeScore);
            questionScore.setWriteScore(writeScore);
            //设置试卷的返回值
            paperAnswerPartDto.setQuestionSize(questionSize);
            paperAnswerPartDto.setQuestionScore(questionScore);
            paperAnswerDto.setPaperAnswerPartDto(paperAnswerPartDto);
            return paperAnswerDto;
        }
    }

    @Override
    public StudentAnswerDto findStudentResult
            (String testId, String paperId, String studentId) {
        StudentAnswerDto studentAnswer = judgePaperMapper
                .findStudentAnswer(testId, paperId, studentId);
        String questionResult = studentAnswer.getQuestionResult();
        String[] split = questionResult.split("@fg@");
        /* 对学生各题型进行分类
        1.1 分别创建studentAnswerPartDto中的列表和字符串列表列表的接收对象
        1.2 在循环遍历中依次在字符串列表或字符串列表列表中追加答案
         */
        StudentAnswerPartDto studentAnswerPartDto = new StudentAnswerPartDto();
        List<String> selectResultList = new ArrayList<>();
        List<String> judgeResultList = new ArrayList<>();
        List<List<String>> OneFillResult = new ArrayList<>();
        for (String s : split) {
            if (s.contains("@￥#1@")) {
                //选择题
                String[] selectResult = s.split("@￥#1@");
                Collections.addAll(selectResultList, selectResult);
                studentAnswerPartDto.setSelectResultList(selectResultList);
            } else if (s.contains("@￥#2@")) {
                //判断题
                String[] judgeResult = s.split("@￥#2@");
                Collections.addAll(judgeResultList, judgeResult);
                studentAnswerPartDto.setJudgeResultList(judgeResultList);
            } else if (s.contains("@￥#3@")) {
                //填空题
                String[] fillResultGroup = s.split("@￥#3@");
                for (String s1 : fillResultGroup) {
                    String[] split1 = s1.split("#1!#");
                    List<String> OneFillResultLittle = new ArrayList<>(Arrays.asList(split1));
                    OneFillResult.add(OneFillResultLittle);
                }
                studentAnswerPartDto.setFillResultList(OneFillResult);
            }
        }
        studentAnswer.setStudentAnswerPartDto(studentAnswerPartDto);
        return studentAnswer;
    }

    @Override
    public JudgeAnswerPartDto judgePaper
            (StudentAnswerDto studentAnswerDto, PaperAnswerDto paperAnswerDto) {
        StudentAnswerPartDto studentAnswerPartDto = studentAnswerDto.getStudentAnswerPartDto();
        PaperAnswerPartDto paperAnswerPartDto = paperAnswerDto.getPaperAnswerPartDto();
        //学生的分数
        QuestionScore questionScore = new QuestionScore();
        //分数列表
        List<Integer> selectScoreList = new ArrayList<>();
        List<Integer> judgeScoreList = new ArrayList<>();
        List<Integer> OneFillScoreList = new ArrayList<>();
        //各题型总分
        int selectScore = 0;
        int judgeScore = 0;
        int fillScore = 0;
        //选择题Judge
        List<String> studentSelectResultList = studentAnswerPartDto.getSelectResultList();
        List<String> paperSelectResultList = paperAnswerPartDto.getSelectResultList();
        List<String> SelectStatusList = new ArrayList<>();
        for (int i = 0; i < paperSelectResultList.size(); i++) {
            if (paperSelectResultList.get(i)
                    .equals(studentSelectResultList.get(i))) {
                SelectStatusList.add("true");
                selectScoreList.add(paperAnswerPartDto.getQuestionScore().getSelectScoreList().get(i));
                selectScore = selectScore + paperAnswerPartDto.getQuestionScore().getSelectScoreList().get(i);
            } else {
                SelectStatusList.add("false");
                selectScoreList.add(0);
            }
        }
        //判断题Judge
        List<String> studentJudgeResultList = studentAnswerPartDto.getJudgeResultList();
        List<String> paperJudgeResultList = paperAnswerPartDto.getJudgeResultList();
        List<String> JudgeStatusList = new ArrayList<>();
        for (int i = 0; i < paperJudgeResultList.size(); i++) {
            if (paperJudgeResultList.get(i).equals("T")) {
                paperJudgeResultList.set(i, "√");
                if (paperJudgeResultList.get(i).equals(studentJudgeResultList.get(i))) {
                    JudgeStatusList.add("true");
                    judgeScoreList.add(paperAnswerPartDto.getQuestionScore().getJudgeScoreList().get(i));
                    judgeScore = judgeScore + paperAnswerPartDto.getQuestionScore().getJudgeScoreList().get(i);
                } else {
                    JudgeStatusList.add("false");
                    judgeScoreList.add(0);
                }
            } else if (paperJudgeResultList.get(i).equals("F")) {
                paperJudgeResultList.set(i, "X");
                if (paperJudgeResultList.get(i).equals(studentJudgeResultList.get(i))) {
                    judgeScoreList.add(paperAnswerPartDto.getQuestionScore().getJudgeScoreList().get(i));
                    judgeScore = judgeScore + paperAnswerPartDto.getQuestionScore().getJudgeScoreList().get(i);
                    JudgeStatusList.add("true");
                } else {
                    JudgeStatusList.add("false");
                    judgeScoreList.add(0);
                }
            }
        }
        //填空题Judge
        List<List<String>> studentFillResultList = studentAnswerPartDto.getFillResultList();
        List<List<String>> paperFillResultList = paperAnswerPartDto.getFillResultList();
        List<List<String>> lists = new ArrayList<>();
        for (int i = 0; i < paperFillResultList.size(); i++) {
            List<String> strings = paperFillResultList.get(i);
            List<String> strings1 = studentFillResultList.get(i);
            List<String> list = new ArrayList<>();
            int rightCount = 0;
            for (int i1 = 0; i1 < strings.size(); i1++) {
                if (strings.get(i1).equals(strings1.get(i1))) {
                    list.add("true");
                    rightCount++;
                } else
                    list.add("false");
            }
            OneFillScoreList.add(paperAnswerPartDto.getQuestionScore().getFillScoreList().get(i) * rightCount / strings.size());
            fillScore = fillScore + paperAnswerPartDto.getQuestionScore().getFillScoreList().get(i) * rightCount / strings.size();
            lists.add(list);
        }
        //返回得分
        questionScore.setSelectScoreList(selectScoreList);
        questionScore.setJudgeScoreList(judgeScoreList);
        questionScore.setFillScoreList(OneFillScoreList);
        //各题型总分值
        questionScore.setSelectScore(selectScore);
        questionScore.setFillScore(fillScore);
        questionScore.setJudgeScore(judgeScore);
        // 判卷完成，并且将对错信息一一对应后存入正误列表
        JudgeAnswerPartDto judgeAnswerPartDto = new JudgeAnswerPartDto();
        judgeAnswerPartDto.setSelectResultStatusList(SelectStatusList);
        judgeAnswerPartDto.setJudgeResultStatusList(JudgeStatusList);
        judgeAnswerPartDto.setFillResultStatusList(lists);
        judgeAnswerPartDto.setQuestionScore(questionScore);
        return judgeAnswerPartDto;
    }

    @Override
    public TwoData studentScoreUpdate(JudgeAnswerPartDto judgeAnswerPartDto,
                                      PaperAnswerDto paperAnswerDto,
                                      StudentAnswerDto studentAnswerDto) {
        Integer writeSize = paperAnswerDto.getPaperAnswerPartDto().getQuestionSize().getWriteSize();
        StringBuilder stringBuffer = new StringBuilder();
        int stringBufferSize;
        QuestionScore questionScore = judgeAnswerPartDto.getQuestionScore();
        //选择
        for (Integer integer : questionScore.getSelectScoreList()) {
            stringBuffer.append(integer);
            stringBuffer.append("@￥#S@");
        }
        stringBufferSize = stringBuffer.length();
        stringBuffer.delete(stringBufferSize - 5, stringBufferSize);
        stringBuffer.append("@fg@");
        //判断
        for (Integer integer : questionScore.getJudgeScoreList()) {
            stringBuffer.append(integer);
            stringBuffer.append("@￥#J@");
        }
        stringBufferSize = stringBuffer.length();
        stringBuffer.delete(stringBufferSize - 5, stringBufferSize);
        stringBuffer.append("@fg@");
        //填空
        for (Integer integer : questionScore.getFillScoreList()) {
            stringBuffer.append(integer);
            stringBuffer.append("@￥#F@");
        }
        stringBufferSize = stringBuffer.length();
        stringBuffer.delete(stringBufferSize - 5, stringBufferSize);
        stringBuffer.append("@fg@");
        //简答
        if (writeSize > 0) {
            for (int i = 0; i < writeSize; i++) {
                stringBuffer.append("-1");
                stringBuffer.append("@￥#W@");
            }
            stringBufferSize = stringBuffer.length();
            stringBuffer.delete(stringBufferSize - 5, stringBufferSize);
        }
        boolean flag;
        int length = changeScoreMapper.getScoreString(studentAnswerDto.getStudentId(), studentAnswerDto.getTestId())
                .getStudentScore().length();
        if (length > 0) {
            TwoData twoData = new TwoData();
            twoData.setFlag(false);
            return twoData;
        } else {
            flag = judgePaperMapper
                    .studentScoreInsert(stringBuffer.toString(),
                            studentAnswerDto.getTestId(),
                            studentAnswerDto.getPaperId(),
                            studentAnswerDto.getStudentId());
            TwoData twoData = new TwoData();
            twoData.setFlag(flag);
            twoData.setStringBuilder(stringBuffer);
            return twoData;
        }
    }

}
