package org.javaboy.fileupload;

import org.javaboy.fileupload.dao.ConnEntityMapper;
import org.javaboy.fileupload.dao.TopicEntityMapper;
import org.javaboy.fileupload.entity.ConnEntity;
import org.javaboy.fileupload.entity.TopicEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

public class DataResponseDao {
    @Autowired
    TopicEntityMapper topicEntityMapper;
    @Autowired
    ConnEntityMapper connEntityMapper;
    public List<TopicEntity> queryData(String uid){
        List<ConnEntity> connEntities = connEntityMapper.selectByUid(uid);
        List<TopicEntity> result = connEntities.stream().map(connEntity -> {
            TopicEntity topic = topicEntityMapper.selectByPrimaryKey(connEntity.getTid());
            return topic;
        }).collect(Collectors.toList());
        return result;
    }
}
