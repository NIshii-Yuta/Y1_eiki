package com.college.yi.bookmaneger.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.college.yi.bookmaneger.model.Book;

@Service
public class BookService {
    
    public List<Book> getAllbooksample(){
        List<Book> books = new ArrayList<>();
               
        books.add(new Book(1L,"ONE PIECE第一巻","尾田 栄一郎","集英社",LocalDate.of(1997,12, 24),10));
        books.add(new Book(2L,"ONE PIECE第二巻","尾田 栄一郎","集英社",LocalDate.of(1998,4, 3),10));
        books.add(new Book(3L,"ONE PIECE第三巻","尾田 栄一郎","集英社",LocalDate.of(1998,6, 4),10));
        books.add(new Book(4L,"ONE PIECE第四巻","尾田 栄一郎","集英社",LocalDate.of(1998,8, 4),10));
        books.add(new Book(5L,"ONE PIECE第五巻","尾田 栄一郎","集英社",LocalDate.of(1998,10, 2),10));
        return books;
        
        
    }

    
    }
    


