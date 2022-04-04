package de.fakultaet73.galvanize.carapp.api.carappapi.dataLoader;

import de.fakultaet73.galvanize.carapp.api.carappapi.documents.Booking;
import de.fakultaet73.galvanize.carapp.api.carappapi.documents.Car;
import de.fakultaet73.galvanize.carapp.api.carappapi.services.SequenceGeneratorService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@AllArgsConstructor
@Component
public class BookingDataLoader {

    SequenceGeneratorService sequenceGeneratorService;

    public List<Booking> getBookings() {
        List<Booking> testBookingsList = new ArrayList<>();
        Random rand = new Random();

        long[] carIds = {1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L, 10L};
        long[] userIds = {6L, 7L, 8L, 9L, 10L};

        int bookingId = 1;
        for (long car : carIds) {
            for (int i = 0; i < rand.nextInt(5) + 1; i++) {
                Booking bookingTmp = Booking.builder()
                        .carId(car)
                        .userId(userIds[rand.nextInt(userIds.length)])
                        .from(LocalDate.of(2022, i + 2, i + 1))
                        .until(LocalDate.of(2022, i + 2, i + 3))
                        .build();
                bookingTmp.setId(sequenceGeneratorService.generateSequence(Booking.SEQUENCE_NAME));
                testBookingsList.add(bookingTmp);
            }
        }
        return testBookingsList;
    }

}
