package com.igb.web.online_library.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Entity
@Data
public class Patron {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    @NotBlank(message="Name shouldn't be empty.")
    private String name;

    @NotBlank(message="Contact information shouldn't be empty.")
    private String contactInformation;



}


