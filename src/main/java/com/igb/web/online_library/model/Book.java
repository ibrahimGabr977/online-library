package com.igb.web.online_library.model;

import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Entity
@Data
public class Book {

  

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long  id;

    @NotBlank
    private String title;

    @NotBlank
    private String author;

    @NotNull
    private Date publicationYear;

    @NotBlank
    @Size(min = 13, max = 13, message = "ISBN must consist of 13 digits.")
    private String isbn;



 




}


