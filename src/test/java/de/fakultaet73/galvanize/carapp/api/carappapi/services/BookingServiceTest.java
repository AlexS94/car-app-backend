package de.fakultaet73.galvanize.carapp.api.carappapi.services;

import de.fakultaet73.galvanize.carapp.api.carappapi.Booking;
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
       // when(bookingService.bookingExists(anyLong())).thenReturn(true);
        when(bookingRepository.save(any(Booking.class))).thenReturn(validBooking);

        // Act
        Booking result = bookingService.addBooking(validBooking);

        // Assert
      //  verify(userService).addCarIdToHostUser(anyLong(), anyLong());
        assertEquals(validBooking, result);
    }

//    @Test
//    void addCar_Car_hostUserIdNotExists_throwsHostNotExistsException() {
//        // Arrange
//        when(userService.userExists(anyLong())).thenReturn(false);
//
//        // Act // Assert
//        assertThrows(HostNotExistsException.class,
//                () -> carService.addCar(validCar),
//                "Exception was expected");
//    }



    @Test
    void updateBooking_booking_returnsCar() {
        // Arrange
//        when(carRepository.existsCarByIdAndHostUserId(anyLong(), anyLong()))
//                .thenReturn(true);
        when(bookingRepository.save(any(Booking.class))).thenReturn(validBooking);

        // Act
        Optional<Booking> result = bookingService.updateBooking(validBooking);

        // Assert
        assertEquals(Optional.of(validBooking), result);
    }


//    @Test
//    void updateCar_hostUserIdDeviating_returnsNotFound() {
//        // Arrange
//        when(carRepository.existsCarByIdAndHostUserId(anyLong(), anyLong()))
//                .thenReturn(false);
//
//        // Act
//        Optional<Car> result = carService.updateCar(validCar);
//
//        // Assert
//        assertTrue(result.isEmpty());
//    }



    @Test
    void deleteCar_id_returnsTrue() {
        // Arrange
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(validBooking));
//      doNothing().when(userService).deleteCarIdFromHostUser(anyLong(), anyLong());
        doNothing().when(bookingRepository).deleteById(anyLong());

        // Act
        boolean result = bookingService.deleteBooking(validBooking.getId());

        // Assert
        assertTrue(result);
//        verify(carRepository).deleteById(anyLong());
//        verify(userService).deleteCarIdFromHostUser(anyLong(), anyLong());
    }

    @Test
    void deleteCar_id_notExists_returnsFalse() {
        // Arrange
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.empty());
        // Act
        boolean result = bookingService.deleteBooking(validBooking.getId());

        // Assert
        assertFalse(result);
    }

/*
    @Test
    void deleteAllCarsWithHostUserId_returnNothing() {
        //Arrange
        doNothing().when(carRepository).deleteAllByHostUserId(anyLong());

        //Act
        carService.deleteAllCarsWithHostUserId(validCar.getHostUserId());

        // Assert
        verify(carRepository).deleteAllByHostUserId(anyLong());
    }
*/
}