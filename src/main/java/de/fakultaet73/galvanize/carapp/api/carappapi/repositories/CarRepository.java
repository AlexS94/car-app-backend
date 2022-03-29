package de.fakultaet73.galvanize.carapp.api.carappapi.repositories;

import de.fakultaet73.galvanize.carapp.api.carappapi.documents.Car;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface CarRepository extends MongoRepository<Car, Long> {

    List<Car> findByAddressContains(String city);

    List<Car> findByHostUserId(long hostUserId);

    boolean existsCarByIdAndHostUserId(long id, long hostUserId);

    void deleteAllByHostUserId(long hostUserId);

    boolean existsCarById(long id);

    List<Car> findAllByHostUserId(long hostUserId);
}
