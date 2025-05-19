package com.college.yi.bookmaneger;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDate;
import java.util.Arrays;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.college.yi.bookmaneger.config.SecurityConfig;
import com.college.yi.bookmaneger.controller.ApiController;
import com.college.yi.bookmaneger.model.Book;
import com.college.yi.bookmaneger.service.BookService;
import com.college.yi.bookmaneger.service.CustomUserDetailsService;
@Import(SecurityConfig.class)
@WebMvcTest(ApiController.class)
class BookControllerTest {

    @Autowired 
    MockMvc mockMvc;
    
    @MockBean 
    BookService bookService;
    
    @MockBean
    CustomUserDetailsService customUserDetailsService;
    
    private final Book sample = new Book(
            1L, "タイトル", "著者", "出版社",
            LocalDate.of(2025,5,2), 10
        );

   
     

        @Test
        @DisplayName("ADMIN or USER は200＋JSON")
        @WithMockUser(roles = {"USER"})
        void userGetsList() throws Exception {
            when(bookService.getAllBooks())
               .thenReturn(Arrays.asList(sample));

            mockMvc.perform(get("/api/books")
                   .accept(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$[0].id").value(1));
        }

        @Test
        @DisplayName("未認証は401 Unauthorized")
        @WithAnonymousUser
        void anonymousGets401() throws Exception {
            mockMvc.perform(get("/api/books"))
                   .andExpect(status().isUnauthorized());
        }
    

    

        private String payload = """
        {
          "title":"new","author":"A","publisher":"P",
          "publishedDate":"2025-06-01","stock":5
        }
        """;

        @Test
        @DisplayName("ADMIN は201 Created")
        @WithMockUser(roles = {"ADMIN"}) 
        void adminCreates() throws Exception {
            Book created = new Book(42L, "new", "A", "P",
                                    LocalDate.of(2025,6,1), 5);
            when(bookService.createBook(any(Book.class)))
            .thenReturn(created);

            mockMvc.perform(post("/api/books")
                   .with(user("admin").roles("ADMIN"))
                   .with(csrf()) 
                   .contentType(MediaType.APPLICATION_JSON)
                   .content(payload))
               .andExpect(status().isCreated())
               .andExpect(header().string("Location", "http://localhost/api/books/42"))
               .andExpect(jsonPath("$.id").value(42));
        }

        @Test
        @DisplayName("USER は403 Forbidden")
        @WithMockUser(roles = {"USER"})
        void userForbidden() throws Exception {
            Book created = new Book(42L, "new", "A", "P",
                    LocalDate.of(2025,6,1), 5);
            
            when(bookService.createBook(any(Book.class)))
            .thenReturn(created);
            
            mockMvc.perform(post("/api/books")
                   .with(user("user").roles("USER"))  
                   .with(csrf()) 
                   .contentType(MediaType.APPLICATION_JSON)
                   .content(payload))
               .andExpect(status().isForbidden());
        }

        @Test
        @DisplayName("未認証は401 Unauthorized")
        @WithAnonymousUser
        void anonymousCreates401() throws Exception {
            mockMvc.perform(post("/api/books")
                   .with(csrf())
                   .contentType(MediaType.APPLICATION_JSON)
                   .content(payload))
               .andExpect(status().isUnauthorized());
        }
    

  

        private String updateJson = """
        {
          "title":"upd","author":"B","publisher":"Q",
          "publishedDate":"2025-07-01","stock":15
        }
        """;

        @Test
        @DisplayName("ADMIN は200 OK")
        void adminUpdates() throws Exception {
            when(bookService.updateBook(any(Long.class), any(Book.class)))
               .thenReturn(sample);

            mockMvc.perform(put("/api/books/1")
                   .with(user("admin").roles("ADMIN")) 
                   .with(csrf())
                   .contentType(MediaType.APPLICATION_JSON)
                   .content(updateJson))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.title").value("タイトル"));
        }

        @Test
        @DisplayName("USER は403 Forbidden")
        void userCannotUpdate() throws Exception {
            mockMvc.perform(put("/api/books/1")
                   .with(user("user").roles("USER"))
                   .with(csrf())
                   .contentType(MediaType.APPLICATION_JSON)
                   .content(updateJson))
               .andExpect(status().isForbidden());
        }
    

   

        @Test
        @DisplayName("ADMIN は204 No Content")
        void adminDeletes() throws Exception {
            doNothing().when(bookService).deleteBook(1L);

            mockMvc.perform(
                    delete("/api/books/1")
                      .with(user("admin").roles("ADMIN"))  // ← ここ
                      .with(csrf())                        // ← ここ
                )
                .andExpect(status().isNoContent());
        }

        @Test
        @DisplayName("USER は403 Forbidden")
        void userCannotDelete() throws Exception {
            mockMvc.perform(
                    delete("/api/books/1")
                      .with(user("user").roles("USER"))
                      .with(csrf())
                )
                .andExpect(status().isForbidden());
        }

        @Test
        @DisplayName("未認証は401 Unauthorized")
        void anonymousDeletes401() throws Exception {
            mockMvc.perform(
                    delete("/api/books/1")
                      .with(csrf())
                )
                .andExpect(status().isUnauthorized());
        }
        
    }

