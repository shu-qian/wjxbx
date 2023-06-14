package com.jdsbbmq.wjxbx.dao.entity;

import com.jdsbbmq.wjxbx.bean.questionnaire.Questionnaire;
import com.jdsbbmq.wjxbx.service.QuestionnaireService;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

import java.util.Date;
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "questionnaire")
public class QuestionnaireEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String id;
    private String projectId;
    private String questionnaireName;
    private String questionnaireDescription;
    private Date creationDate;
    private Date startTime;
    private Date endTime;
    private String status;
    private int Count;

    //复制
    public QuestionnaireEntity(Questionnaire questionnaire){
        if(questionnaire == null){
                //无参构造
            return ;
        }else{
            this.id = questionnaire.getId();
            this.projectId = questionnaire.getProjectId();
            this.questionnaireName = questionnaire.getQuestionnaireName();
            this.questionnaireDescription = questionnaire.getQuestionnaireDescription();
            this.creationDate = questionnaire.getCreationDate();
            this.startTime = questionnaire.getStartTime();
            this.endTime = questionnaire.getEndTime();
            this.status = questionnaire.getStatus();
            this.Count = questionnaire.getCount();
        }
    }
}
