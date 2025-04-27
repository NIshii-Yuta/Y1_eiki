package com.college.yi.bookmaneger.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.college.yi.bookmaneger.model.Book;
import com.college.yi.bookmaneger.service.BookService;

@RestController
public class ApiController {
    
 private final BookService bookService = new BookService();
    
    
  
    @GetMapping("/api/books")
    public List<Book>getBooks(){
        return bookService.getAllbooksample();
    }

}
