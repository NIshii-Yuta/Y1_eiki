package com.college.yi.bookmaneger.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.college.yi.bookmaneger.service.BookService;
@Controller
//@RequestMapping("/api/books")
public class BookController {
    
    private final BookService bookService;
    
    
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }
    
    @GetMapping("/books")
    public String showBookPage() {
        return "index";
    }

}
