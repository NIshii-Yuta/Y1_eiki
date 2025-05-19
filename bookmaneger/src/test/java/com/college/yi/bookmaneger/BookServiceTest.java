package com.college.yi.bookmaneger;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.college.yi.bookmaneger.entity.BookEntity;
import com.college.yi.bookmaneger.model.Book;
import com.college.yi.bookmaneger.repository.BookMapper;
import com.college.yi.bookmaneger.service.BookService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    private BookMapper bookMapper;

    @InjectMocks
    private BookService bookService;

    private BookEntity sampleEntity;
    private Book sampleModel;

    @BeforeEach
    void setUp() {
        sampleEntity = new BookEntity();
        sampleEntity.setId(1L);
        sampleEntity.setTitle("タイトル");
        sampleEntity.setAuthor("著者");
        sampleEntity.setPublisher("出版社");
        sampleEntity.setPublishedDate(LocalDate.of(2025, 5, 2));
        sampleEntity.setStock(10);

        sampleModel = new Book(
            1L, "タイトル", "著者", "出版社",
            LocalDate.of(2025, 5, 2), 10
        );
    }

    @Nested
    @DisplayName("findAllBooks")
    class FindAllBooks {

        @Test
        @DisplayName("全件存在するとき、モデルリストを返す")
        void whenEntitiesExist_thenReturnsModelList() {
            when(bookMapper.findAll())
                .thenReturn(Arrays.asList(sampleEntity));

            List<Book> result = bookService.getAllBooks();
            assertThat(result).hasSize(1)
                              .first()
                              .usingRecursiveComparison()
                              .isEqualTo(sampleModel);

            verify(bookMapper, times(1)).findAll();
        }

        @Test
        @DisplayName("エンティティが空のとき、404例外を投げる")
        void whenNoEntities_thenThrows404() {
            when(bookMapper.findAll()).thenReturn(Collections.emptyList());
            assertThrows(ResponseStatusException.class,
                () -> bookService.getAllBooks(),
                "書籍が存在しません"
            );
            verify(bookMapper).findAll();
        }
    }

    @Nested
    @DisplayName("createBook")
    class CreateBook {

        @Test
        @DisplayName("正常に登録し、ID付きモデルを返す")
        void whenCreate_thenReturnsCreatedModel() {
            // モックは insert 後にエンティティの ID がセットされていることを想定
            doAnswer(invocation -> {
                BookEntity arg = invocation.getArgument(0);
                arg.setId(42L);
                return 1;
            }).when(bookMapper).insert(any(BookEntity.class));

            Book toCreate = new Book(null, "新規", "著者", "出版社",
                                     LocalDate.of(2025, 6, 1), 5);
            Book created = bookService.createBook(toCreate);

            assertThat(created.getId()).isEqualTo(42L);
            assertThat(created.getTitle()).isEqualTo("新規");
            verify(bookMapper, times(1)).insert(any(BookEntity.class));
        }
    }

    @Nested
    @DisplayName("updateBook")
    class UpdateBook {

        @Test
        @DisplayName("既存書籍があれば更新してモデルを返す")
        void whenExists_thenUpdateAndReturn() {
            when(bookMapper.findById(1L)).thenReturn(sampleEntity);
            when(bookMapper.update(any(BookEntity.class))).thenReturn(1);

            Book updatedInput = new Book(null, "改訂版", "著者", "出版社",
                                         LocalDate.of(2025, 7, 1), 15);
            Book updated = bookService.updateBook(1L, updatedInput);

            assertThat(updated.getTitle()).isEqualTo("改訂版");
            assertThat(updated.getStock()).isEqualTo(15);
            verify(bookMapper).findById(1L);
            verify(bookMapper).update(any(BookEntity.class));
        }

        @Test
        @DisplayName("存在しない場合は404例外")
        void whenNotExist_thenThrows404() {
            when(bookMapper.findById(999L)).thenReturn(null);
            assertThrows(ResponseStatusException.class,
                () -> bookService.updateBook(999L, sampleModel)
            );
            verify(bookMapper).findById(999L);
        }

        @Test
        @DisplayName("更新件数が1でない場合は500例外")
        void whenUpdateCountNotOne_thenThrows500() {
            when(bookMapper.findById(1L)).thenReturn(sampleEntity);
            when(bookMapper.update(any(BookEntity.class))).thenReturn(0);
            assertThrows(ResponseStatusException.class,
                () -> bookService.updateBook(1L, sampleModel)
            );
            verify(bookMapper).update(any(BookEntity.class));
        }
    }

    @Nested
    @DisplayName("deleteBook")
    class DeleteBook {

        @Test
        @DisplayName("既存書籍があれば削除成功")
        void whenExists_thenDeletes() {
            when(bookMapper.findById(1L)).thenReturn(sampleEntity);
            when(bookMapper.deleteById(1L)).thenReturn(1);

            assertDoesNotThrow(() -> bookService.deleteBook(1L));
            verify(bookMapper).findById(1L);
            verify(bookMapper).deleteById(1L);
        }

        @Test
        @DisplayName("存在しない場合は404例外")
        void whenNotExist_thenThrows404() {
            when(bookMapper.findById(1L)).thenReturn(null);
            assertThrows(ResponseStatusException.class,
                () -> bookService.deleteBook(1L)
            );
            verify(bookMapper).findById(1L);
        }

        @Test
        @DisplayName("削除件数が1でない場合は500例外")
        void whenDeleteCountNotOne_thenThrows500() {
            when(bookMapper.findById(1L)).thenReturn(sampleEntity);
            when(bookMapper.deleteById(1L)).thenReturn(0);
            assertThrows(ResponseStatusException.class,
                () -> bookService.deleteBook(1L)
            );
            verify(bookMapper).deleteById(1L);
        }
    }
}
