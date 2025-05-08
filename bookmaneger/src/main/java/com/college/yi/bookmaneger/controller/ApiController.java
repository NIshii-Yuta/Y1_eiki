package com.college.yi.bookmaneger.controller;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.college.yi.bookmaneger.model.Book;
import com.college.yi.bookmaneger.service.BookService;

@RestController
@RequestMapping("/api/books") 
public class ApiController {
    
 private final BookService bookService;
    
    public ApiController(BookService bookService) {
        this.bookService = bookService;
    }
  
    @GetMapping
    public List<Book>getBooks(){
        return bookService.getAllBooks();
    }
    
    //新規登録
    @PostMapping
    public ResponseEntity<Book> createBook(@RequestBody Book book){
        Book created = bookService.createBook(book);
        
        // Location ヘッダに新規リソースの URL を設定
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(created.getId())
                    .toUri();

        return ResponseEntity.created(uri).body(created);
    }
    
    /** 更新 */
    @PutMapping("/{id}")
    public ResponseEntity<Book> updateBook(
            @PathVariable Long id,
            @RequestBody Book book) {

        Book updated = bookService.updateBook(id, book);
        return ResponseEntity.ok(updated);
    }
    
    //削除
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
        // 204 No Content
        return ResponseEntity.noContent().build();
    }
}
