package com.igb.web.online_library.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.igb.web.online_library.model.BorrowingRecord;

public interface BorrowingRecordRepository  extends CrudRepository<BorrowingRecord, Long>{

    Optional<BorrowingRecord> findRecordByBookIdAndPatronId(Long bookId, Long patronId);
}
