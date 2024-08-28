package com.igb.web.online_library.service;

import java.util.Date;

import org.springframework.stereotype.Service;

import com.igb.web.online_library.model.Book;
import com.igb.web.online_library.model.BorrowingRecord;
import com.igb.web.online_library.model.Patron;
import com.igb.web.online_library.repositories.BorrowingRecordRepository;

import jakarta.transaction.Transactional;




@Service
public class BorrowingRecordService {

    private final BookService bookService;
    private final PatronService patronsService;
    private final BorrowingRecordRepository recordRepository;

    public BorrowingRecordService(BookService bookService, PatronService patronsService,
            BorrowingRecordRepository recordRepository) {
        this.bookService = bookService;
        this.patronsService = patronsService;
        this.recordRepository = recordRepository;
    }



    @Transactional
    public void createNewBorrowingRecord(Long bookId, Long patronId) {

        Book targetBook = bookService.findBookBy(bookId);

        if (targetBook == null)
            throw new RuntimeException("No Book Found with this id to process borrowing.");

        Patron targetPatron = patronsService.findPatronBy(patronId);

        if (targetPatron == null)
            throw new RuntimeException("No Patron Found with this id to process borrowing.");


         addNewRecordWith(targetBook, targetPatron);


    }

    private void addNewRecordWith(Book book, Patron patron) {
        BorrowingRecord borrowingRecord = new BorrowingRecord();

        borrowingRecord.setBook(book);
        borrowingRecord.setPatron(patron);
        borrowingRecord.setBorrowDate(new Date());

        recordRepository.save(borrowingRecord);

        
    }


    @Transactional
    public void returnABook(Long bookId, Long patronId){
        Book targetBook = bookService.findBookBy(bookId);

        if (targetBook == null)
            throw new RuntimeException("No Book Found with this id to process borrowing.");

        Patron targetPatron = patronsService.findPatronBy(patronId);

        if (targetPatron == null)
            throw new RuntimeException("No Patron Found with this id to process borrowing.");



       BorrowingRecord foundRecord = recordRepository.findRecordByBookIdAndPatronId(bookId, patronId)
       .orElseThrow(() -> new RuntimeException("No record for these book and patron."));



            foundRecord.setReturnDate(new Date());
           recordRepository.save(foundRecord);


    }





}
