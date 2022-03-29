package de.fakultaet73.galvanize.carapp.api.carappapi.services;

import de.fakultaet73.galvanize.carapp.api.carappapi.documents.Booking;
import de.fakultaet73.galvanize.carapp.api.carappapi.exceptions.BookingAlreadyExistsException;
import de.fakultaet73.galvanize.carapp.api.carappapi.exceptions.CarNotExistsException;
import de.fakultaet73.galvanize.carapp.api.carappapi.exceptions.UserNotExistsException;
import de.fakultaet73.galvanize.carapp.api.carappapi.exceptions.InvalidBookingException;
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

    private enum MethodType {ADD, UPDATE;}

    public boolean bookingExists(long id) {
        return bookingRepository.existsBookingById(id);
    }

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
        validateBooking(booking, MethodType.ADD);
        booking.setId(sequenceGeneratorService.generateSequence(Booking.SEQUENCE_NAME));
        return bookingRepository.save(booking);
    }

    public Optional<Booking> updateBooking(Booking booking) {
        if (bookingRepository.existsBookingByIdAndCarIdAndUserId(
                booking.getId(), booking.getCarId(), booking.getUserId())) {
            validateBooking(booking, MethodType.UPDATE);
            return Optional.of(bookingRepository.save(booking));
        }
        return Optional.empty();
    }

    public boolean deleteBooking(long id) {
        if (bookingExists(id)) {
            bookingRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public void deleteAllWithCarId(long id) {
        bookingRepository.deleteAllByCarId(id);
    }

    public void deleteAllWithUserId(long id) {
        bookingRepository.deleteAllByUserId(id);
    }

    private void validateBooking(Booking booking, MethodType type) {
        if (booking.getUntil().isBefore(booking.getFrom())) {
            throw new InvalidBookingException("Until date is before from date");
        }

        if (type == MethodType.ADD) {
            if (!carService.carExists(booking.getCarId())) {
                throw new CarNotExistsException("Car does not exist");
            }
            if (!userService.userExists(booking.getUserId())) {
                throw new UserNotExistsException("User does not exist");
            }
        }

        if (bookingSlotNotAvailable(booking)) {
            throw new BookingAlreadyExistsException("Booking for Timeslot already exists");
        }
    }

    private boolean bookingSlotNotAvailable(Booking booking) {
        return bookingRepository.findByCarId(booking.getCarId()).stream().anyMatch(
                b -> booking.getFrom().isBefore(b.getUntil()) && b.getFrom().isBefore(booking.getUntil()));
    }

}
