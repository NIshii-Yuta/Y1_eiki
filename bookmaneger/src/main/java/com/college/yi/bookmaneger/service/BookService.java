package com.college.yi.bookmaneger.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.college.yi.bookmaneger.entity.BookEntity;
import com.college.yi.bookmaneger.model.Book;
import com.college.yi.bookmaneger.repository.BookMapper;

@Service
public class BookService {

    private final BookMapper bookMapper;

    public BookService(BookMapper bookMapper) {
        this.bookMapper = bookMapper;
    }

    public List<Book> getAllBooks() {
        List<BookEntity> entities = bookMapper.findAll();
        if (entities.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "書籍が存在しません");
        }

        return entities
                .stream().map(entity -> new Book(entity.getId(), entity.getTitle(), entity.getAuthor(),
                        entity.getPublisher(), entity.getPublishedDate(), entity.getStock()))
                .collect(Collectors.toList());
    }

    // 新規登録
    public Book createBook(Book model) {
        // モデルをエンティティに変換
        BookEntity entity = new BookEntity();
        entity.setTitle(model.getTitle());
        entity.setAuthor(model.getAuthor());
        entity.setPublisher(model.getPublisher());
        entity.setPublishedDate(model.getPublishedDate());
        entity.setStock(model.getStock());

        // DBへinsertする
        bookMapper.insert(entity);

        // エンティティをモデルに戻して返却
        return new Book(entity.getId(), entity.getTitle(), entity.getAuthor(), entity.getPublisher(),
                entity.getPublishedDate(), entity.getStock());
    }

    // 更新
    public Book updateBook(Long id, Book model) {
        // 1) 存在確認
        BookEntity existing = bookMapper.findById(id);
        if (existing == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "書籍が存在しません");
        }

        // 2) model の値を既存エンティティにセット
        existing.setTitle(model.getTitle());
        existing.setAuthor(model.getAuthor());
        existing.setPublisher(model.getPublisher());
        existing.setPublishedDate(model.getPublishedDate());
        existing.setStock(model.getStock());

        // 3) 更新実行
        int updated = bookMapper.update(existing);
        if (updated != 1) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "更新に失敗しました");
        }

        // 4) 更新後のエンティティを model に変換して返却
        return new Book(existing.getId(), existing.getTitle(), existing.getAuthor(), existing.getPublisher(),
                existing.getPublishedDate(), existing.getStock());
    }

    // 削除
    public void deleteBook(Long id) {
        // 存在チェック
        BookEntity existing = bookMapper.findById(id);
        if (existing == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "書籍が存在しません");
        }
        // 削除実行
        int deleted = bookMapper.deleteById(id);
        if (deleted != 1) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "削除に失敗しました");
        }
    }
}

