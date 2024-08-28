package com.igb.web.online_library;



import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.igb.web.online_library.controllers.BorrowingRecordController;
import com.igb.web.online_library.service.BorrowingRecordService;

@WebMvcTest(BorrowingRecordController.class)
@WithMockUser(username = "user", password = "1234567")
public class BorrowingRecordControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BorrowingRecordService recordService;




    @Test
    void testCreateNewRecord_nonExistingBook() throws Exception {
        Long bookId = 1L;
        Long patronId = 1L;

        doThrow(new RuntimeException("No Book Found with this id to process borrowing.")).when(recordService)
        .createNewBorrowingRecord(bookId, patronId);


        mockMvc.perform(post("/api/borrow/"+bookId+"/patron/"+patronId)
        .with(csrf())) 
        .andExpect(status().isNotFound())
        .andExpect(content().string("No Book Found with this id to process borrowing."));

    }


    @Test
    void testCreateNewRecord_nonExistingPatron() throws Exception {
        Long bookId = 1L;
        Long patronId = 1L;

        doThrow(new RuntimeException("No Patron Found with this id to process borrowing.")).when(recordService)
        .createNewBorrowingRecord(bookId, patronId);


        mockMvc.perform(post("/api/borrow/"+bookId+"/patron/"+patronId)
        .with(csrf())) 
        .andExpect(status().isNotFound())
        .andExpect(content().string("No Patron Found with this id to process borrowing."));

    }



    @Test
    void testCreateNewRecord() throws Exception {
        Long bookId = 1L;
        Long patronId = 1L;

        doNothing().when(recordService).createNewBorrowingRecord(bookId, patronId);


        mockMvc.perform(post("/api/borrow/"+bookId+"/patron/"+patronId)
        .with(csrf())) 
        .andExpect(status().isNotFound())
        .andExpect(content().string("Book borrowed successfully."));

    }


    @Test
    void testReturnBook_existing() throws Exception {
        Long bookId = 1L;
        Long patronId = 1L;
        

        doNothing().when(recordService).createNewBorrowingRecord(bookId, patronId);


        mockMvc.perform(post("/api/borrow/"+bookId+"/patron/"+patronId)
        .with(csrf())) 
        .andExpect(status().isNotFound())
        .andExpect(content().string("Book returned successfully."));

    }



    @Test
    void testReturnBook_nonExisting() throws Exception {
        Long bookId = 1L;
        Long patronId = 1L;

        doThrow(new RuntimeException("No record for these book and patron."))
        .when(recordService).createNewBorrowingRecord(bookId, patronId);


        mockMvc.perform(post("/api/borrow/"+bookId+"/patron/"+patronId)
        .with(csrf())) 
        .andExpect(status().isNotFound())
        .andExpect(content().string("Book borrowed successfully."));

    }

  

}
