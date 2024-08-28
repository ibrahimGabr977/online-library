package com.igb.web.online_library.repositories;

import org.springframework.data.repository.CrudRepository;

import com.igb.web.online_library.model.Book;


public interface BookRepository extends CrudRepository<Book, Long> {

}
