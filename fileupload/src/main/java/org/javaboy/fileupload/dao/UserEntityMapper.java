package org.javaboy.fileupload.dao;

import java.util.List;
import org.javaboy.fileupload.entity.UserEntity;

public interface UserEntityMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user
     *
     * @mbg.generated Wed Jan 20 15:46:03 CST 2021
     */
    int insert(UserEntity record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user
     *
     * @mbg.generated Wed Jan 20 15:46:03 CST 2021
     */
    List<UserEntity> selectAll();

    String selectById(String uid);
}