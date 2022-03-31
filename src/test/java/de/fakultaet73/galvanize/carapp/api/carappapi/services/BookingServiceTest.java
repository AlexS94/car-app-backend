package de.fakultaet73.galvanize.carapp.api.carappapi.services;

import de.fakultaet73.galvanize.carapp.api.carappapi.model.Address;
import de.fakultaet73.galvanize.carapp.api.carappapi.documents.Booking;
import de.fakultaet73.galvanize.carapp.api.carappapi.model.CarDetails;
import de.fakultaet73.galvanize.carapp.api.carappapi.documents.Car;
import de.fakultaet73.galvanize.carapp.api.carappapi.exceptions.BookingAlreadyExistsException;
import de.fakultaet73.galvanize.carapp.api.carappapi.exceptions.CarNotExistsException;
import de.fakultaet73.galvanize.carapp.api.carappapi.exceptions.UserNotExistsException;
import de.fakultaet73.galvanize.carapp.api.carappapi.exceptions.InvalidBookingException;
import de.fakultaet73.galvanize.carapp.api.carappapi.repositories.BookingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookingServiceTest {

    BookingService bookingService;

    @Mock
    UserService userService;

    @Mock
    CarService carService;

    @Mock
    BookingRepository bookingRepository;

    @Mock
    SequenceGeneratorService sequenceGeneratorService;

    Booking validBooking;

    @BeforeEach
    void setUp() {
        bookingService = new BookingService(bookingRepository, carService, userService, sequenceGeneratorService);
        validBooking = Booking.builder()
                .userId(1L)
                .carId(2L)
                .from(LocalDate.of(2022, 2, 2))
                .until(LocalDate.of(2022, 3, 2))
                .build();
    }

    @Test
    void getBooking_id_returnsBookingOptional() {
        // Arrange
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(validBooking));

        // Act
        Optional<Booking> result = bookingService.getBooking(1);

        // Assert
        assertEquals(Optional.of(validBooking), result);
    }

    @Test
    void getBooking_id_notExists_returnsEmptyOptional() {
        // Arrange
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act
        Optional<Booking> result = bookingService.getBooking(1);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void getBookingsByUserId_userId_returnsBookingList() {
        // Arrange
        List<Booking> bookingList = List.of(
                validBooking, validBooking
        );
        when(bookingRepository.findByUserId(anyLong())).thenReturn(bookingList);

        // Act
        List<Booking> result = bookingService.getBookingsByUserId(1);

        // Assert
        assertEquals(bookingList, result);
    }

    @Test
    void getBookingsByUserId_userId_returnsEmptyList() {
        // Arrange
        when(bookingRepository.findByUserId(anyLong())).thenReturn(new ArrayList<>());

        // Act
        List<Booking> result = bookingService.getBookingsByUserId(1);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void getBookingByCarId_id_returnsBookingList() {
        // Arrange
        List<Booking> bookingList = List.of(
                validBooking, validBooking
        );

        when(bookingRepository.findByCarId(anyLong())).thenReturn(bookingList);

        // Act
        List<Booking> result = bookingService.getBookingsByCarId(1);

        // Assert
        assertEquals(bookingList, result);
    }

    @Test
    void getBookingsByCarId_userId_returnsEmptyList() {
        // Arrange
        when(bookingRepository.findByCarId(anyLong())).thenReturn(new ArrayList<>());

        // Act
        List<Booking> result = bookingService.getBookingsByCarId(1);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void addBooking_booking_returnsBooking() {
        // Arrange
        Car car = Car.builder()
                .hostUserId(1L)
                .make("Volkswagen")
                .model("Golf")
                .type("Hatchback")
                .year(2013)
                .details(new CarDetails("diesel", 4, 5, 115, "manual"))
                .pricePerDay(15)
                .address(new Address("Musterstreet", "12", "Berlin", 12345))
                .build();
        when(carService.carExists(anyLong())).thenReturn(true);
        when(userService.userExists(anyLong())).thenReturn(true);
        when(bookingRepository.save(any(Booking.class))).thenReturn(validBooking);

        //Act
        Booking result = bookingService.addBooking(validBooking);

        // Assert
        assertEquals(validBooking, result);
    }

    @Test
    void addBooking_booking_carNotExists_throwsCarNotExistsException() {
        // when(bookingService.bookingExists(anyLong())).thenReturn(true);
        when(carService.carExists(anyLong())).thenReturn(false);
        // Assert
        assertThrows(CarNotExistsException.class,
                () -> bookingService.addBooking(validBooking),
                "Exception was expected");

    }

    @Test
    void addBooking_booking_UserIdNotExists_throwsUserNotExistsException() {
        // Arrange
        when(carService.carExists(anyLong())).thenReturn(true);
        when(userService.userExists(anyLong())).thenReturn(false);

        // Act // Assert
        assertThrows(UserNotExistsException.class,
                () -> bookingService.addBooking(validBooking),
                "Exception was expected");
    }

    @Test
    void addBooking_booking_timeslotNotAvailable_throwsBookingAlreadyExistsException() {
        // Arrange
        when(carService.carExists(anyLong())).thenReturn(true);
        when(userService.userExists(anyLong())).thenReturn(true);

        long carId = 13;
        when(bookingRepository.findByCarId(anyLong())).thenReturn(
                List.of(
                        new Booking(1, 2L, carId, LocalDate.of(2022, 2, 1), LocalDate.of(2022, 2, 5)),
                        new Booking(2, 4L, carId, LocalDate.of(2022, 3, 3), LocalDate.of(2022, 3, 12)),
                        new Booking(3, 6L, carId, LocalDate.of(2022, 4, 1), LocalDate.of(2022, 4, 15))
                )
        );

        // Act // Assert
        assertThrows(BookingAlreadyExistsException.class,
                () -> bookingService.addBooking(validBooking),
                "Exception was expected");
    }

    @Test
    void addBooking_booking_fromDateAfterUntilDate_throwsInvalidBookingException() {
        // Arrange
        Booking invalidBooking = new Booking(1, 2L, 13L, LocalDate.of(2022, 2, 15), LocalDate.of(2022, 2, 10));

        // Act // Assert
        assertThrows(InvalidBookingException.class,
                () -> bookingService.addBooking(invalidBooking),
                "Exception was expected");
    }

    @Test
    void updateBooking_booking_returnsCar() {
        // Arrange
        when(bookingRepository.existsBookingByIdAndCarIdAndUserId(anyLong(), anyLong(), anyLong())).thenReturn(true);
        when(bookingRepository.save(any(Booking.class))).thenReturn(validBooking);

        // Act
        Optional<Booking> result = bookingService.updateBooking(validBooking);

        // Assert
        assertEquals(Optional.of(validBooking), result);
    }

    @Test
    void updateBooking_bookingNotExists_returnsOptionalEmpty() {
        // Arrange
        when(bookingRepository.existsBookingByIdAndCarIdAndUserId(anyLong(), anyLong(), anyLong())).thenReturn(false);

        // Act
        Optional<Booking> result = bookingService.updateBooking(validBooking);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void updateBooking_newBookingIsInvalid_throwsInvalidBookingException() {
        // Arrange
        when(bookingRepository.existsBookingByIdAndCarIdAndUserId(anyLong(), anyLong(), anyLong())).thenReturn(true);
        Booking invalidBooking = new Booking(1, 2L, 13L, LocalDate.of(2022, 2, 15), LocalDate.of(2022, 2, 10));

        // Act // Assert
        assertThrows(InvalidBookingException.class,
                () -> bookingService.updateBooking(invalidBooking),
                "Exception was expected");
    }

    @Test
    void deleteBooking_id_returnsTrue() {
        // Arrange
        when(bookingRepository.existsBookingById(anyLong())).thenReturn(true);
        doNothing().when(bookingRepository).deleteById(anyLong());

        // Act
        boolean result = bookingService.deleteBooking(validBooking.getId());

        // Assert
        assertTrue(result);
    }

    @Test
    void deleteBooking_id_notExists_returnsFalse() {
        // Arrange
        when(bookingRepository.existsBookingById(anyLong())).thenReturn(false);
        // Act
        boolean result = bookingService.deleteBooking(validBooking.getId());

        // Assert
        assertFalse(result);
    }

}