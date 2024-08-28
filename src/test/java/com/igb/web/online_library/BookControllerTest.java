package com.igb.web.online_library;

import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.igb.web.online_library.controllers.BookController;
import com.igb.web.online_library.model.Book;
import com.igb.web.online_library.service.BookService;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;


@WebMvcTest(BookController.class)
@WithMockUser(username = "user", password = "1234567")
class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    @Test
    void testFindAllBooks_EmptyList() throws Exception {
        Mockito.when(bookService.findAllBooks()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/books")).andExpect(status().isNoContent());
    }

    @Test
    void testFindAllBooks_NonEmptyList() throws Exception {

        Book book = getTestBook();

        List<Book> books = List.of(book);

        Mockito.when(bookService.findAllBooks()).thenReturn(books);

        mockMvc.perform(get("/api/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(book.getId()))
                .andExpect(jsonPath("$[0].title").value(book.getTitle()))
                .andExpect(jsonPath("$[0].author").value(book.getAuthor()))
                .andExpect(jsonPath("$[0].publicationYear").value(formatDateOf(book.getPublicationYear())))
                .andExpect(jsonPath("$[0].isbn").value(book.getIsbn()));
    }

    @Test
    void testFindBookById_existingId() throws Exception {
        Long id = 1L;
        Book book = getTestBook();

        Mockito.when(bookService.findBookBy(id)).thenReturn(book);

        mockMvc.perform(get("/api/books/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(book.getId()))
                .andExpect(jsonPath("$.title").value(book.getTitle()))
                .andExpect(jsonPath("$.author").value(book.getAuthor()))
                .andExpect(jsonPath("$.publicationYear").value(formatDateOf(book.getPublicationYear())))
                .andExpect(jsonPath("$.isbn").value(book.getIsbn()));

    }

    @Test
    void testFindBookById_nonExistingId() throws Exception {
        Long id = 1L;

        Mockito.when(bookService.findBookBy(id)).thenReturn(null);

        mockMvc.perform(get("/api/books/1"))
                .andExpect(status().isNotFound());

    }

    @Test
    void testAddNewBook_validBookData() throws Exception {
        Book book = getTestBook();

        when(bookService.addNew(book)).thenReturn(book);

        mockMvc.perform(post("/api/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(book))
                .with(csrf()))
                .andExpect(status().isCreated())
                .andExpect(header().string(HttpHeaders.LOCATION, "/api/books/1"));
    }


    @Test
    void testAddNewBook_invalidBookData() throws Exception {
        Book book = getTestBook();
        book.setIsbn("232");


        mockMvc.perform(post("/api/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(book))
                .with(csrf()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.isbn").value("ISBN must consist of 13 digits."));
    }


    @Test
    void updateBook_existingBook() throws Exception {

        Long id = 1L;
        Book book = getTestBook();

        when(bookService.updateBookWith(id, book)).thenReturn(book);

        mockMvc.perform(put("/api/books/"+id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(book))
                .with(csrf())) 
                .andExpect(status().isNoContent());

    }

    @Test
    void updateBook_nonExistingBook() throws Exception {

        Long id = 1L;
        Book book = getTestBook();

        when(bookService.updateBookWith(id, book)).thenThrow(new RuntimeException("Book not found"));

        mockMvc.perform(put("/api/books/"+id)
        .contentType(MediaType.APPLICATION_JSON)
        .content(new ObjectMapper().writeValueAsString(book))
        .with(csrf())) 
        .andExpect(status().isNotFound());

    }


    @Test
    void deleteBook_existingBook() throws Exception {

        Long id = 1L;

        doNothing().when(bookService).deleteBookBy(id);

        mockMvc.perform(delete("/api/books/"+id)
                .with(csrf()))
                .andExpect(status().isNoContent());

    }


    @Test
    void deleteBook_nonExistingBook() throws Exception {

        Long id = 1L;
        

        doThrow(new RuntimeException("Book not found")).when(bookService).deleteBookBy(id);


        mockMvc.perform(delete("/api/books/"+id)
        .with(csrf())) 
        .andExpect(status().isNotFound());

    }


    


    private String formatDateOf(Date date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
        String expectedDate = formatter.format(date.toInstant().atOffset(ZoneOffset.UTC));

        return expectedDate.replace("Z", "+00:00");
    }

    private Book getTestBook() {
        Book book = new Book();
        book.setId(1L);
        book.setTitle("Title");
        book.setAuthor("Author");
        book.setPublicationYear(new Date());
        book.setIsbn("1234567890123");

        return book;

    }
}
