package de.fakultaet73.galvanize.carapp.api.carappapi.documents;

import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class BookingTest {

    @Test
    void create_new_booking() {
        // Arrange
        long carId = 2;
        long userId = 15;
        LocalDate from = LocalDate.of(2021,2,2);
        LocalDate until = LocalDate.of(2021,3,2);

        Booking booking = new Booking(1,userId, carId, from, until);

        // Assert
        assertEquals(1, booking.getId());
        assertEquals(carId, booking.getCarId());
        assertEquals(userId, booking.getUserId());
        assertEquals(from, booking.getFrom());
        assertEquals(until, booking.getUntil());
    }

}
