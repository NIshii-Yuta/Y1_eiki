package com.college.yi.bookmaneger.entity;

import java.time.LocalDate;

import lombok.Data;

@Data
public class BookEntity {
    private Long id;
    private String title;
    private String author;
    private String publisher;
    private LocalDate publishedDate;
    private int stock;
}
