package com.igb.web.online_library;


import java.util.Collections;
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
import com.igb.web.online_library.controllers.PatronController;
import com.igb.web.online_library.model.Patron;
import com.igb.web.online_library.service.PatronService;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;


@WebMvcTest(PatronController.class)
@WithMockUser(username = "user", password = "1234567")
class PatronControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PatronService patronService;

    @Test
    void testFindAllPatrons_EmptyList() throws Exception {
        Mockito.when(patronService.findAllPatrons()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/patrons")).andExpect(status().isNoContent());
    }

    @Test
    void testFindAllPatrons_NonEmptyList() throws Exception {

        Patron patron = getTestPatron();

        List<Patron> patrons = List.of(patron);

        Mockito.when(patronService.findAllPatrons()).thenReturn(patrons);

        mockMvc.perform(get("/api/patrons"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(patron.getId()))
                .andExpect(jsonPath("$[0].name").value(patron.getName()))
                .andExpect(jsonPath("$[0].contactInformation").value(patron.getContactInformation()));
    }



    @Test
    void testFindPatronById_existingId() throws Exception {
        Long id = 1L;
        Patron patron = getTestPatron();

        Mockito.when(patronService.findPatronBy(id)).thenReturn(patron);

        mockMvc.perform(get("/api/patrons/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(patron.getId()))
                .andExpect(jsonPath("$.name").value(patron.getName()))
                .andExpect(jsonPath("$.contactInformation").value(patron.getContactInformation()));
    }



    @Test
    void testFindPatronById_nonExistingId() throws Exception {
        Long id = 1L;

        Mockito.when(patronService.findPatronBy(id)).thenReturn(null);

        mockMvc.perform(get("/api/patrons/"+id))
                .andExpect(status().isNotFound());

    }




    @Test
    void testAddNewPatron_validPatronData() throws Exception {
        Patron patron = getTestPatron();

        when(patronService.addNew(patron)).thenReturn(patron);

        mockMvc.perform(post("/api/patrons")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(patron))
                .with(csrf()))
                .andExpect(status().isCreated())
                .andExpect(header().string(HttpHeaders.LOCATION, "/api/patrons/1"));
    }


    @Test
    void testAddNewPatron_invalidPatronData() throws Exception {
        Patron patron = getTestPatron();
        patron.setName(" ");


        mockMvc.perform(post("/api/patrons")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(patron))
                .with(csrf()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.name").value("Name shouldn't be empty."));
    }



    @Test
    void updatePatron_existingPatron() throws Exception {

        Long id = 1L;
        Patron patron = getTestPatron();

        when(patronService.updatePatronWith(id, patron)).thenReturn(patron);

        mockMvc.perform(put("/api/patrons/"+id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(patron))
                .with(csrf()))
                .andExpect(status().isNoContent());

    }


    @Test
    void updatePatron_nonExistingPatron() throws Exception {

        Long id = 1L;
        Patron patron = getTestPatron();

        when(patronService.updatePatronWith(id, patron)).thenThrow(new RuntimeException("Patron not found"));

        mockMvc.perform(put("/api/patrons/"+id)
        .contentType(MediaType.APPLICATION_JSON)
        .content(new ObjectMapper().writeValueAsString(patron))
        .with(csrf())) 
        .andExpect(status().isNotFound());

    }


    @Test
    void deletePatron_existingPatron() throws Exception {

        Long id = 1L;

        doNothing().when(patronService).deletePatronBy(id);

        mockMvc.perform(delete("/api/patrons/"+id)
                .with(csrf()))
                .andExpect(status().isNoContent());

    }


    @Test
    void deletePatron_nonExistingPatron() throws Exception {

        Long id = 1L;
        

        doThrow(new RuntimeException("Patron not found")).when(patronService).deletePatronBy(id);


        mockMvc.perform(delete("/api/patrons/"+id)
        .with(csrf())) 
        .andExpect(status().isNotFound());

    }


    



    private Patron getTestPatron() {
        Patron patron = new Patron();
        patron.setId(1L);
        patron.setName("name");
        patron.setContactInformation("contactInformation");

        return patron;

    }
}
