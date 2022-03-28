package de.fakultaet73.galvanize.carapp.api.carappapi.repositories;

import de.fakultaet73.galvanize.carapp.api.carappapi.documents.Booking;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface BookingRepository extends MongoRepository<Booking, Long> {

    List<Booking> findByUserId(long userId);

    List<Booking> findByCarId(long carId);

    void deleteAllByCarId(long carId);

    void deleteAllByUserId(long userId);

    boolean existsBookingById(long id);

    boolean existsBookingByIdAndCarIdAndUserId(long id, long carId, long userId);
}
