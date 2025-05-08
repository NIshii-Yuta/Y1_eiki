package com.college.yi.bookmaneger.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
@Controller
@RequestMapping("/books")
public class BookController {
    
    @GetMapping
    public String showBookPage(Model model) {
        return "index";
    }

}
