package com.igb.web.online_library.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.igb.web.online_library.model.Book;
import com.igb.web.online_library.repositories.BookRepository;

import jakarta.transaction.Transactional;

@SuppressWarnings({ "null", "unchecked" })
@Service
@Validated
public class BookService {

    private static final String BOOKS = "books";
    private static final String ALL_BOOKS = "allBooks";
    private final BookRepository booksRepository;
    private final CacheManager cacheManager;

    public BookService(BookRepository booksRepository, CacheManager cacheManager) {
        this.booksRepository = booksRepository;
        this.cacheManager = cacheManager;
    }

    @Cacheable(value = BOOKS, key = ALL_BOOKS)
    public List<Book> findAllBooks() {
        return (List<Book>) booksRepository.findAll();
    }


    public Book findBookBy(Long id) {
        return booksRepository.findById(id).orElse(null);

    }

    public Book addNew(Book book) {
        List<Book> allBooks = allBooksCache() == null ? new ArrayList<>() : allBooksCache();
        allBooks.add(book);
        cacheManager.getCache(BOOKS).put(ALL_BOOKS, allBooks);

        return booksRepository.save(book);
    }



    @Transactional
    public Book updateBookWith(Long id, Book updatedBook) {

        List<Book> allBooksCache = allBooksCache();

        Book existingBook = booksRepository.findById(id).orElseThrow(() -> new RuntimeException("Book not found"));

        updateBookFields(existingBook, updatedBook);

        updateBookIfFoundOnCache(existingBook, allBooksCache);

        return booksRepository.save(existingBook);
    }


    
    private void updateBookFields(Book book, Book updatedBook) {
        book.setTitle(updatedBook.getTitle());
        book.setAuthor(updatedBook.getAuthor());
        book.setPublicationYear(updatedBook.getPublicationYear());
        book.setIsbn(updatedBook.getIsbn());
    }

    private void updateBookIfFoundOnCache(Book book, List<Book> allBooksCache) {
        if (allBooksCache != null) {
            for (int i = 0; i < allBooksCache.size(); i++) {
                if (allBooksCache.get(i).getId().equals(book.getId())) {
                    allBooksCache.set(i, book);
                    cacheManager.getCache(BOOKS).put(ALL_BOOKS, allBooksCache);
                    break;
                }
            }
        }

    }

    @Transactional
    public void deleteBookBy(Long id) {

        if (!booksRepository.existsById(id))
            throw new RuntimeException("Book not found");

        booksRepository.deleteById(id);

        List<Book> allBooks = allBooksCache();

        if (allBooks != null) {
            allBooks.removeIf(book -> book.getId().equals(id));

            cacheManager.getCache(BOOKS).put(ALL_BOOKS, allBooks);
        }

    }

    private List<Book> allBooksCache() {
        return cacheManager.getCache(BOOKS).get(ALL_BOOKS, List.class);
    }

}
