package org.javaboy.fileupload;


import java.util.List;


public class DataResponse{
    String userName;

    List<TopicData> dataContent;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public List<TopicData> getDataContent() {
        return dataContent;
    }

    public void setDataContent(List<TopicData> dataContent) {
        this.dataContent = dataContent;
    }
}
