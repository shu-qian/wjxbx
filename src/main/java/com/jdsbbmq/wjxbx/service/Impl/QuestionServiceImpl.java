package com.jdsbbmq.wjxbx.service.Impl;

import com.google.gson.Gson;
import com.jdsbbmq.wjxbx.bean.question.DesignRequest;
import com.jdsbbmq.wjxbx.bean.question.Question;
import com.jdsbbmq.wjxbx.bean.question.UpdateQuestionStarRequest;
import com.jdsbbmq.wjxbx.dao.QuestionEntityMapper;
import com.jdsbbmq.wjxbx.dao.entity.QuestionEntity;
import com.jdsbbmq.wjxbx.service.QuestionService;
import jakarta.annotation.Resource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class QuestionServiceImpl implements QuestionService {
    @Resource
    private QuestionEntityMapper questionEntityMapper;

    /*
        查询
     */
    //根据所给的问卷Id，查找其设计的问题
    @Override
    @Async("asyncServiceExecutor")
    public List<Question> selectQuestionById(String id) {
        Gson gson = new Gson();
        List<QuestionEntity> questionEntityList = questionEntityMapper.selectQuestionById(id);
        List<Question> questionList = new ArrayList<>();
        for (QuestionEntity questionEntity : questionEntityList) {
            Question question = gson.fromJson(questionEntity.getQuestionContent(), Question.class);
            question.setStar(questionEntity.getStar());
            questionList.add(question);
        }
        return questionList;
    }

    //查询个人题库中的所有问题
    @Override
    @Async("asyncServiceExecutor")
    public List<Question> selectPrivateQuestion(String userId) {
        Gson gson = new Gson();
        List<Question> questionList = new ArrayList<>();
        List<QuestionEntity> questionEntityList= questionEntityMapper.selectPrivateQuestion(userId);
        for (QuestionEntity questionEntity : questionEntityList) {
            Question question = gson.fromJson(questionEntity.getQuestionContent(), Question.class);
            question.setStar(1);
            questionList.add(question);
        }
        return questionList;
    }


        /*
            增删改
        */

    //增加

    //设计问卷问题
    @Override
    @Async("asyncServiceExecutor")
    @Transactional(rollbackFor = RuntimeException.class)
    public int insertDesignQuestion(DesignRequest designRequest) {
        try {
            Gson gson = new Gson();
            List<QuestionEntity> questionEntityList = new ArrayList<>();
            questionEntityMapper.deleteQuestionById(designRequest.getId());
            for (int i = 0; i < designRequest.getQuestions().size(); i++) {
                QuestionEntity questionEntity = new QuestionEntity(designRequest.getId(),designRequest.getQuestions().get(i).getQuestionId(), i + 1,designRequest.getQuestions().get(i).getStar(), gson.toJson(designRequest.getQuestions().get(i)));
                questionEntityList.add(questionEntity);
            }
            if (questionEntityList.size() == 0) {
                return 1;
            }
            int b = questionEntityMapper.insertDesignQuestion(questionEntityList);
            if (b != questionEntityList.size()) {
                throw new RuntimeException("插入设计问卷的问题失败");
            }
            return 1;
        } catch (Exception e) {
            throw new RuntimeException("插入设计问卷的问题失败");
        }
    }

    //将问卷问题放入个人题库
    @Override
    @Async("asyncServiceExecutor")
    @Transactional(rollbackFor = RuntimeException.class)
    public int insertPrivateQuestion(UpdateQuestionStarRequest updateQuestionStarRequest) {
        try {
            String questionContent=questionEntityMapper.selectQuestionContentById(updateQuestionStarRequest.getQuestionId());
            QuestionEntity questionEntity=new QuestionEntity();
            questionEntity.setId(updateQuestionStarRequest.getUserId());
            questionEntity.setQuestionId(updateQuestionStarRequest.getQuestionId());
            questionEntity.setQuestionContent(questionContent);
            int a=questionEntityMapper.insertPrivateQuestion(questionEntity);
            int b=questionEntityMapper.updateStarOnQuestion(updateQuestionStarRequest.getQuestionId());
            if(a!=1||questionContent==null||b!=1){
                throw new RuntimeException("插入个人题库失败");
            }
            return 1;
        }catch (Exception e){
            throw new RuntimeException("插入个人题库失败");
        }
    }

    //删除

    //将问卷问题从个人题库中拿出
    @Override
    @Async("asyncServiceExecutor")
    @Transactional(rollbackFor = RuntimeException.class)
    public int deletePrivateQuestionById(String questionId) {
        try{
            int a=questionEntityMapper.deletePrivateQuestionById(questionId);
            int b=questionEntityMapper.updateStarOffQuestion(questionId);
            if(a!=1||b!=1){
                throw new RuntimeException("删除个人题库失败");
            }
            return 1;
        }catch (Exception e){
            throw new RuntimeException("删除个人题库失败");
        }
    }
}
