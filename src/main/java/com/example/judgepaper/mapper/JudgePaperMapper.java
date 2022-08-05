package com.example.judgepaper.mapper;

import com.example.judgepaper.Dto.ResponseDto.Question;
import com.example.judgepaper.Dto.ResponseDto.StudentAnswerDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface JudgePaperMapper {

    /**
     * 根据试卷id找问题的正确答案列表
     * @param id
     * @param rightFlag
     * @return List<Question>
     */
    List<Question> findQuestionByPaperId(String id, Integer rightFlag);

    /**
     * 根据学生id、试卷id、考试id查找对应学生的答题情况
     * @param testId
     * @param paperId
     * @param studentId
     * @return StudentAnswerDto
     */
    StudentAnswerDto findStudentAnswer(@Param("testid") String testId,
                                       @Param("paperid") String paperId,
                                       @Param("studentid") String studentId);

    /**
     * 自动判卷后，并将分值插入
     * @param TestScore
     * @param testId
     * @param paperId
     * @param studentId
     * @return
     */
    boolean studentScoreInsert(@Param("score") String TestScore,
                               @Param("testid") String testId,
                               @Param("paperid") String paperId,
                               @Param("studentid") String studentId);
}
