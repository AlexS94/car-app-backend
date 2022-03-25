package de.fakultaet73.galvanize.carapp.api.carappapi.services;

import de.fakultaet73.galvanize.carapp.api.carappapi.Booking;
import de.fakultaet73.galvanize.carapp.api.carappapi.documents.Car;
import de.fakultaet73.galvanize.carapp.api.carappapi.repositories.BookingRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class BookingService {

    BookingRepository bookingRepository;
    CarService carService;
    UserService userService;
    SequenceGeneratorService sequenceGeneratorService;

    public Optional<Booking> getBooking(long id) {
        return bookingRepository.findById(id);
    }

    public List<Booking> getBookingsByUserId(long id) {
        return bookingRepository.findByUserId(id);
    }

    public List<Booking> getBookingsByCarId(long id) {
        return bookingRepository.findByCarId(id);
    }

    public Booking addBooking(Booking booking) {
        return null;
    }

    public Optional<Booking> updateBooking(Booking booking) {
        return null;
    }

    public boolean deleteBooking(long id) {
        return false;
    }

}
