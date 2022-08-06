package com.example.judgepaper.mapper;

import com.example.judgepaper.Dto.ResponseDto.Change.ChangeQuestionList;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface ChangeScoreMapper {
    ChangeQuestionList getScoreString(@Param("studentid") String studentId,
                                      @Param("testid") String testId);

    boolean updateScore(@Param("studentid") String studentId,
                        @Param("testid") String testId,
                        @Param("score") String score,
                        @Param("sum") Double scoreS);
}
