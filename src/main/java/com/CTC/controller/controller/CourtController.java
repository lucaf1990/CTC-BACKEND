package com.CTC.controller.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.CTC.entity.Court;
import com.CTC.payload.CourtDTO;
import com.CTC.service.service.CourtServiceImplementation;




@RestController
@RequestMapping("/api/courts")
public class CourtController {

    private final CourtServiceImplementation courtService;

    @Autowired
    public CourtController(CourtServiceImplementation courtService) {
        this.courtService = courtService;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Court> createCourt(@RequestBody CourtDTO courtPayload) {
        Court createdCourt = courtService.createCourt(courtPayload);
        return new ResponseEntity<>(createdCourt, HttpStatus.CREATED);
    }

    @PutMapping("/{courtId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Court> updateCourt(@PathVariable Long courtId, @RequestBody Court updatedCourt) {
        Court updated = courtService.updateCourt(courtId, updatedCourt);
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }
    @PutMapping("/cambiaPrezzi/{courtId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Court> changeCourtPrice(@PathVariable Long courtId, @PathVariable Double price,@PathVariable Double priceSocio) {
        Court updated = courtService.changePrice(courtId, price,priceSocio );
        return new ResponseEntity<Court>(updated, HttpStatus.OK);
    }
    @DeleteMapping("/{courtId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteCourt(@PathVariable Long courtId) {
        courtService.deleteCourt(courtId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/{courtId}")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<Court> getSpecificCourt(@PathVariable Long courtId) {
        Court court = courtService.getSpecificField(courtId);
        return new ResponseEntity<>(court, HttpStatus.OK);
    }
}