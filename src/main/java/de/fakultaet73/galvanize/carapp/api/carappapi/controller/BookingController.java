package de.fakultaet73.galvanize.carapp.api.carappapi.controller;

import de.fakultaet73.galvanize.carapp.api.carappapi.documents.Booking;
import de.fakultaet73.galvanize.carapp.api.carappapi.exceptions.BookingAlreadyExistsException;
import de.fakultaet73.galvanize.carapp.api.carappapi.exceptions.InvalidBookingException;
import de.fakultaet73.galvanize.carapp.api.carappapi.services.BookingService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@AllArgsConstructor
@RestController
public class BookingController {

    BookingService bookingService;

    @PostMapping("/booking")
    public Booking addBooking(@Valid @RequestBody Booking booking) {
        return bookingService.addBooking(booking);
    }

    @PutMapping("/booking")
    public ResponseEntity<Booking> updateBooking(@Valid @RequestBody Booking booking) {
        Optional<Booking> optionalBooking = bookingService.updateBooking(booking);
        return optionalBooking.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/booking/{id}")
    public ResponseEntity<Booking> deleteBooking(@PathVariable long id) {
        return bookingService.deleteBooking(id) ? ResponseEntity.ok().build() : ResponseEntity.noContent().build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public void BookingAlreadyExistsExceptionHandler(BookingAlreadyExistsException exception) {
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public void InvalidBookingExceptionHandler(InvalidBookingException exception) {
    }
}
