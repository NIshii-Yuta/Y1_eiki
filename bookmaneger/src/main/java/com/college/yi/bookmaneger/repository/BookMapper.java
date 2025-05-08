package com.college.yi.bookmaneger.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.college.yi.bookmaneger.entity.BookEntity;

@Mapper
public interface BookMapper {
    List<BookEntity>findAll();
    
    BookEntity findById(@Param("id") Long id);
    //新規登録メソッド
   int insert(BookEntity book);
   
   //更新用メソッド
   int update(BookEntity book);
   
   int deleteById(@Param("id")Long id);

}
