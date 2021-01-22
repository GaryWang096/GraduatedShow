package org.javaboy.fileupload.controller;

import org.javaboy.fileupload.dao.ConnEntityMapper;
import org.javaboy.fileupload.dao.TopicEntityMapper;
import org.javaboy.fileupload.entity.ConnEntity;
import org.javaboy.fileupload.json.DataResponse;
import org.javaboy.fileupload.DataResponseDao;
import org.javaboy.fileupload.TopicData;
import org.javaboy.fileupload.dao.UserEntityMapper;
import org.javaboy.fileupload.entity.TopicEntity;
import org.javaboy.fileupload.json.FormDataRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@RestController
public class UploadController {
    SimpleDateFormat sdf = new SimpleDateFormat("/yyyy/MM/dd/");
    @Autowired
    DataResponseDao dataResponseDao;
    @Autowired
    UserEntityMapper userEntityMapper;
    @Autowired
    TopicEntityMapper topicEntityMapper;
    @Autowired
    ConnEntityMapper connEntityMapper;

    @PostMapping("/upload")
    public Map<String,Object> fileupload(FormDataRequest formDataRequest, HttpServletRequest req) {
        String url = saveImg(formDataRequest.getFile(), req);
        if("error".equals(url)){
            return Collections.singletonMap("status","ERROR");
        }

        TopicEntity topicEntity = new TopicEntity();
        topicEntity.setContent(formDataRequest.getContent());
        topicEntity.setImageurl(url);
        topicEntity.setTag(formDataRequest.getTag());
        topicEntity.setTime(new Date());
        topicEntity.setTitle(formDataRequest.getTitle());
        int result = topicEntityMapper.insert(topicEntity);
        if(result == -1){
            return Collections.singletonMap("status","ERROR");
        }
        int tid = topicEntity.getTid();

        ConnEntity connEntity = new ConnEntity();
        connEntity.setTid(tid);
        connEntity.setUid(formDataRequest.getUid());
        result = connEntityMapper.insert(connEntity);
        if(result == -1){
            return Collections.singletonMap("status","ERROR");
        }

        return Collections.singletonMap("status","OK");
    }

    private String saveImg(MultipartFile file, HttpServletRequest req) {
        String format = sdf.format(new Date());
        String realPath = req.getServletContext().getRealPath("/") + format;
        File folder = new File(realPath);
        if (!folder.exists()) {
            folder.mkdirs();
        }
        String oldName = file.getOriginalFilename();
        String newName = UUID.randomUUID().toString() + oldName.substring(oldName.lastIndexOf("."));
        try {
            file.transferTo(new File(folder, newName));
            String url = req.getScheme() + "://" + req.getServerName() + ":" + req.getServerPort() + format + newName;
            return url;
        } catch (IOException e) {
            return "error";
        }
    }

    @RequestMapping("/getAll")
    public DataResponse getAll(String uid){
        List<TopicEntity> data = dataResponseDao.queryData(uid);
        String userName = userEntityMapper.selectById(uid);

        List<TopicData> topicData = data.stream().map(topicEntity -> {
            TopicData topic =  new TopicData();
            topic.setContent(topicEntity.getContent());
            topic.setImgUrl(topicEntity.getImageurl());
            topic.setTag(topicEntity.getTag());
            topic.setTitle(topicEntity.getTitle());
            topic.setTopicId(topicEntity.getTid());
            topic.setCreateTime(topicEntity.getTime());
            return topic;
        }).collect(Collectors.toList());

        DataResponse result = new DataResponse();
        result.setUserName(userName);
        result.setDataContent(topicData);
        return result;
    }

}
