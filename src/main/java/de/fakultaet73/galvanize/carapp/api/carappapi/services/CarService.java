package de.fakultaet73.galvanize.carapp.api.carappapi.services;


import de.fakultaet73.galvanize.carapp.api.carappapi.CarFilter;
import de.fakultaet73.galvanize.carapp.api.carappapi.documents.Car;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CarService {
    public Optional<Car> getCar(long id) {
        return null;
    }


    public List<Car> getAllCars() {
        return null;
    }

    public List<Car> getCars(long hostUserId) {
        return null;
    }

    public List<Car> getFilteredCars(CarFilter filter) {
        return null;
    }
}
