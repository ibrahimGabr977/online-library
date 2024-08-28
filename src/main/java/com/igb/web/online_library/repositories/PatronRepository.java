package com.igb.web.online_library.repositories;

import org.springframework.data.repository.CrudRepository;

import com.igb.web.online_library.model.Patron;

public interface  PatronRepository extends CrudRepository<Patron, Long> {

}
