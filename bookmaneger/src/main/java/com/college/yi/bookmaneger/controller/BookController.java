package com.college.yi.bookmaneger.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.college.yi.bookmaneger.model.Book;
import com.college.yi.bookmaneger.service.BookService;
@Controller
@RequestMapping("/books")
public class BookController {
    
    private final BookService bookService;
    
    
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }
    
    @GetMapping
    public String showBookPage(Model model) {
        model.addAttribute("books", bookService.getAllBooks());
        return "index";
    }
    
    // --- 登録処理（フォーム送信後） ---
    @PostMapping
    public String addBook(@ModelAttribute Book form, Model model) {
        bookService.createBook(form);
        // 登録完了メッセージを渡したいなら addAttribute しても良い
        model.addAttribute("message", "登録完了しました！");
        // PRGパターンでリダイレクト
        return "redirect:/books";
    }

}
