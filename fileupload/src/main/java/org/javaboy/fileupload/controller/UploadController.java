package org.javaboy.fileupload.controller;

import org.javaboy.fileupload.DataResponse;
import org.javaboy.fileupload.DataResponseDao;
import org.javaboy.fileupload.TopicData;
import org.javaboy.fileupload.dao.UserEntityMapper;
import org.javaboy.fileupload.entity.TopicEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

//@RestController
public class UploadController {
    SimpleDateFormat sdf = new SimpleDateFormat("/yyyy/MM/dd/");
    @Autowired
    DataResponseDao dataResponseDao;
    @Autowired
    UserEntityMapper userEntityMapper;

    @PostMapping("/upload")
    public Map<String,Object> fileupload(MultipartFile file, HttpServletRequest req) {
        Map<String, Object> result = new HashMap<>();
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
            result.put("status", "OK");
            result.put("name", oldName);
            result.put("url", url);
        } catch (IOException e) {
            result.put("status", "ERROR");
            result.put("msg", e.getMessage());
        }
        return result;
    }
    @RequestMapping("/getAll")
    public DataResponse getAll(String uid){
        List<TopicEntity> data = dataResponseDao.queryData(uid);
        String userName = userEntityMapper.selectById(uid);

        List<TopicData> topicData = data.stream().map(topicEntity -> {
            TopicData topic =  new TopicData();
            topic.setContent(topicEntity.getContent());
            topic.setImgUrl(topicEntity.getImageurl());
            topic.setTag(topicEntity.getTags());
            topic.setTitle(topicEntity.getTitle());
            topic.setTopicId(topicEntity.getTid());
            return topic;
        }).collect(Collectors.toList());

        DataResponse result = new DataResponse();
        result.setUserName(userName);
        result.setDataContent(topicData);
        return result;
    }

}
