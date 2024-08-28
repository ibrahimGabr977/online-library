package com.igb.web.online_library.controllers;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;
import com.igb.web.online_library.service.BorrowingRecordService;




@RestController
public class BorrowingRecordController {


    
    private final BorrowingRecordService recordService;




    public BorrowingRecordController(BorrowingRecordService recordService) {
        this.recordService = recordService;
    }


    



    @PostMapping("/api/borrow/{bookId}/patron/{patronId}")
    public ResponseEntity<String> borrowABook(@PathVariable Long bookId, @PathVariable Long patronId) {

        try {
            recordService.createNewBorrowingRecord(bookId, patronId);
            return ResponseEntity.status(HttpStatus.CREATED).body("Book borrowed successfully.");
          
        } catch (RuntimeException exception) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());

        }
           
    }







    @PutMapping("/api/return/{bookId}/patron/{patronId}")
    public ResponseEntity<String> returnBook(@PathVariable Long bookId, @PathVariable Long patronId) {



        try {
            recordService.returnABook(bookId, patronId);
            return ResponseEntity.ok("Book returned successfully.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    
    
    }

}
