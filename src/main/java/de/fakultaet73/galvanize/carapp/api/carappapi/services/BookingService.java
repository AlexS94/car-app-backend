package de.fakultaet73.galvanize.carapp.api.carappapi.services;

import de.fakultaet73.galvanize.carapp.api.carappapi.Booking;
import de.fakultaet73.galvanize.carapp.api.carappapi.documents.Car;
import de.fakultaet73.galvanize.carapp.api.carappapi.exceptions.CarNotExistsException;
import de.fakultaet73.galvanize.carapp.api.carappapi.exceptions.HostNotExistsException;
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
        Optional<Car> carOptional = carService.getCar(booking.getCarId());
        if(carOptional.isEmpty()){
            throw new CarNotExistsException("hostUserId does not exist");
        }

        return bookingRepository.save(booking);
    }

    public Optional<Booking> updateBooking(Booking booking) {
        return  Optional.of(bookingRepository.save(booking));
    }

    public boolean deleteBooking(long id) {
        Optional<Booking> booking = getBooking(id);
        if (booking.isPresent()) {
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
}
