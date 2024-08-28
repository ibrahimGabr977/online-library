package com.igb.web.online_library.model;

import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotNull;
import lombok.Data;


@Entity
@Data
public class BorrowingRecord {


    @ManyToOne
    @JoinColumn(name="book_id", nullable = false)
    private Book book;


    @ManyToOne
    @JoinColumn(name="patron_id", nullable = false)
    private Patron patron;

    @Temporal(TemporalType.TIMESTAMP)
    private Date borrowDate;

    @Temporal(TemporalType.TIMESTAMP)
    private Date returnDate;


}
