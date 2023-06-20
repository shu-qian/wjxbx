package com.jdsbbmq.wjxbx.service;

import com.jdsbbmq.wjxbx.bean.QueryRequest;
import com.jdsbbmq.wjxbx.dao.entity.ProjectEntity;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.List;

@Service
public interface ProjectService {
     /*
        查询
     */

    //查询用户下方所有项目
    public List<ProjectEntity> selectAllProject(String userId);

    //根据id查询项目
    public ProjectEntity selectProjectById(String id);

    //根据projectName查询项目
    public List<ProjectEntity> selectProjectByName(String projectName);

    //分页寻找项目
    public List<ProjectEntity> selectProjectByPage(QueryRequest queryRequest);
    /*
        增删改
     */

    // 插入项目
    public int insertProject(ProjectEntity projectEntity);

    //更新项目
    public int updateProject(ProjectEntity projectEntity) throws ParseException;

    //将项目的收藏更新为已收藏
    public int updateStarOnProject(String id);

    //将项目的收藏更新为未收藏
    public int updateStarOffProject(String id);

    //将项目的删除更新为已删除
    public int updateDeletedOnProject(String id);

    //将项目的删除更新为未删除
    public int updateDeletedOffProject(String id);

    // 删除项目
    public int deleteProjectById(String id);

    //清空回收站
    public int deleteAllProjectRecycled(String userId);
}
