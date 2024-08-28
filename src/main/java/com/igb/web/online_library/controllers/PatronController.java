package com.igb.web.online_library.controllers;

import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.igb.web.online_library.model.Patron;
import com.igb.web.online_library.service.PatronService;
import com.igb.web.online_library.util.FieldsCustomValidator;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/patrons")
public class PatronController {

    private final PatronService patronsService;

    public PatronController(PatronService patronsService) {
        this.patronsService = patronsService;
    }

    @GetMapping()
    public ResponseEntity<List<Patron>> findAllPatrons() {
        List<Patron> patrons = patronsService.findAllPatrons();

        if (patrons.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(patrons);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Patron> findPatronById(@PathVariable Long id) {
        Patron patron = patronsService.findPatronBy(id);

        return patron == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(patron);
    }

    @PostMapping()
    public ResponseEntity<Object> addNewPatron(@Valid @RequestBody Patron patron, Errors errors) {
        if (errors.hasErrors())
            return ResponseEntity.badRequest().body(FieldsCustomValidator.getValidationErrorsFrom(errors));

        URI location = URI.create("/api/patrons/" + patronsService.addNew(patron).getId());

        return ResponseEntity.created(location).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updatePatron(@PathVariable Long id, @Valid @RequestBody Patron updatedPatron,
            Errors errors) {
        if (errors.hasErrors())
            return ResponseEntity.badRequest().body(FieldsCustomValidator.getValidationErrorsFrom(errors));

        try {
            patronsService.updatePatronWith(id, updatedPatron);
            return ResponseEntity.noContent().build();

        } catch (RuntimeException exception) {
            return ResponseEntity.notFound().build();
        }
    }




    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePatron(@PathVariable Long id) {

        try {
            patronsService.deletePatronBy(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();


        }
    }

}
