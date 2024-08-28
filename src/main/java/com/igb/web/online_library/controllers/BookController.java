package com.igb.web.online_library.controllers;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.igb.web.online_library.model.Book;
import com.igb.web.online_library.service.BookService;
import com.igb.web.online_library.util.FieldsCustomValidator;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/books")
public class BookController {


    private final BookService bookService;


    public BookController(BookService bookService){
        this.bookService = bookService;
    }






    @GetMapping()
    public ResponseEntity<List<Book>> findAllBooks() {
        List<Book> books = bookService.findAllBooks();

        if (books.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(books);
    }





    @GetMapping("/{id}")
    public ResponseEntity<Book> findBookById(@PathVariable Long id) {
        Book book = bookService.findBookBy(id);

        return book == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(book);
    }







    @PostMapping()
    public ResponseEntity<Object> addNewBook(@Valid @RequestBody Book book, Errors errors) {
        if (errors.hasErrors())
            return ResponseEntity.badRequest().body(FieldsCustomValidator.getValidationErrorsFrom(errors));

        
        URI location = URI.create("/api/books/" + bookService.addNew(book).getId());
        return ResponseEntity.created(location).build();
    }


    


    @PutMapping("/{id}")
    public ResponseEntity<Object> updateBook(@PathVariable Long id, @Valid @RequestBody Book updatedBook, Errors errors) {
        if (errors.hasErrors())
            return ResponseEntity.badRequest().body(FieldsCustomValidator.getValidationErrorsFrom(errors));


        try{
            bookService.updateBookWith(id, updatedBook);
            return ResponseEntity.noContent().build();
        }catch(RuntimeException exception){
            return ResponseEntity.notFound().build();
        }   
    }


    

   


    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        try {
            bookService.deleteBookBy(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();


        }
    }
}