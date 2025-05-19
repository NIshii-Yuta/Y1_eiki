package com.college.yi.bookmaneger.repository;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.college.yi.bookmaneger.entity.UserEntity;

@Mapper
public interface UserMapper {
    
    UserEntity findByUsername(@Param("username") String username);

}
