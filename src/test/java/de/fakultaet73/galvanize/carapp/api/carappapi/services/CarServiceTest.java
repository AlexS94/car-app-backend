package de.fakultaet73.galvanize.carapp.api.carappapi.services;

import de.fakultaet73.galvanize.carapp.api.carappapi.Address;
import de.fakultaet73.galvanize.carapp.api.carappapi.CarDetails;
import de.fakultaet73.galvanize.carapp.api.carappapi.documents.Car;
import de.fakultaet73.galvanize.carapp.api.carappapi.documents.User;
import de.fakultaet73.galvanize.carapp.api.carappapi.exceptions.HostNotExistsException;
import de.fakultaet73.galvanize.carapp.api.carappapi.repositories.CarRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CarServiceTest {

    CarService carService;

    @Mock
    CarRepository carRepository;
    @Mock
    UserService userService;
    @Mock
    BookingService bookingService;


    @Mock
    SequenceGeneratorService sequenceGeneratorService;

    Car validCar;

    @BeforeEach
    void setUp() {
        carService = new CarService(carRepository, userService, bookingService, sequenceGeneratorService);
        validCar = Car.builder()
                .hostUserId(1L)
                .make("Volkswagen")
                .model("Golf")
                .type("Hatchback")
                .year(2013)
                .details(new CarDetails("diesel", 4, 5, 115, "manual"))
                .pricePerDay(15)
                .address(new Address("Musterstreet", "12", "Berlin", 12345))
                .build();
    }

    @Test
    void getCar_id_returnsCarOptional() {
        // Arrange
        when(carRepository.findById(anyLong())).thenReturn(Optional.of(validCar));

        // Act
        Optional<Car> result = carService.getCar(1);

        // Assert
        assertEquals(Optional.of(validCar), result);
    }

    @Test
    void getCar_id_notExists_returnsEmptyOptional() {
        // Arrange
        when(carRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act
        Optional<Car> result = carService.getCar(1);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void getAllCars_noParam_returnsCarList() {
        // Arrange
        List<Car> carList = List.of(validCar);
        when(carRepository.findAll()).thenReturn(carList);

        // Act
        List<Car> result = carService.getAllCars();

        // Assert
        assertEquals(carList, result);
    }

    @Test
    void getAllCars_noParam_returnsEmptyList() {
        // Arrange
        List<Car> carList = new ArrayList<>();
        when(carRepository.findAll()).thenReturn(carList);

        // Act
        List<Car> result = carService.getAllCars();

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void getCarsByCity_city_returnsCarList() {
        // Arrange
        List<Car> carList = List.of(validCar);
        when(carRepository.findByAddressContains(anyString())).thenReturn(carList);

        // Act
        List<Car> result = carService.getCarsByCity("wolfsburg");

        // Assert
        assertEquals(carList, result);
    }

    @Test
    void getCarsByCity_city_returnsEmptyList() {
        // Arrange
        List<Car> carList = new ArrayList<>();
        when(carRepository.findByAddressContains(anyString())).thenReturn(carList);

        // Act
        List<Car> result = carService.getCarsByCity("wolfsburg");

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void getCarsByHostUserId_id_returnsCarList() {
        // Arrange
        List<Car> carList = List.of(validCar);
        when(carRepository.findByHostUserId(anyLong())).thenReturn(carList);

        // Act
        List<Car> result = carService.getCarsByHostUserId(1);

        // Assert
        assertEquals(carList, result);
    }

    @Test
    void getCarsByHostUserId_id_returnsEmptyList() {
        // Arrange
        List<Car> carList = new ArrayList<>();
        when(carRepository.findByHostUserId(anyLong())).thenReturn(carList);

        // Act
        List<Car> result = carService.getCarsByHostUserId(1);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void addCar_Car_returnsCar() {
        // Arrange
        when(userService.userExists(anyLong())).thenReturn(true);
        doNothing().when(userService).addCarIdToHostUser(anyLong(), anyLong());
        when(carRepository.save(any(Car.class))).thenReturn(validCar);
        // Act
        Car result = carService.addCar(validCar);

        // Assert
        verify(userService).addCarIdToHostUser(anyLong(), anyLong());
        assertEquals(validCar, result);
    }

    @Test
    void addCar_Car_hostUserIdNotExists_throwsHostNotExistsException() {
        // Arrange
        when(userService.userExists(anyLong())).thenReturn(false);

        // Act // Assert
        assertThrows(HostNotExistsException.class,
                () -> carService.addCar(validCar),
                "Exception was expected");
    }

    @Test
    void updateCar_car_returnsCar() {
        // Arrange
        when(carRepository.existsCarByIdAndHostUserId(anyLong(), anyLong()))
                .thenReturn(true);
        when(carRepository.save(any(Car.class))).thenReturn(validCar);

        // Act
        Optional<Car> result = carService.updateCar(validCar);

        // Assert
        assertEquals(Optional.of(validCar), result);
    }

    @Test
    void updateCar_hostUserIdDeviating_returnsNotFound() {
        // Arrange
        when(carRepository.existsCarByIdAndHostUserId(anyLong(), anyLong()))
                .thenReturn(false);

        // Act
        Optional<Car> result = carService.updateCar(validCar);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void deleteCar_id_returnsTrue() {
        // Arrange
        when(carRepository.findById(anyLong())).thenReturn(Optional.of(validCar));
        doNothing().when(userService).deleteCarIdFromHostUser(anyLong(), anyLong());
        doNothing().when(carRepository).deleteById(anyLong());

        // Act
        boolean result = carService.deleteCar(validCar.getId());

        // Assert
        assertTrue(result);
        verify(carRepository).deleteById(anyLong());
        verify(userService).deleteCarIdFromHostUser(anyLong(), anyLong());
    }

    @Test
    void deleteCar_id_notExists_returnsFalse() {
        // Arrange
        when(carRepository.findById(anyLong())).thenReturn(Optional.empty());
        // Act
        boolean result = carService.deleteCar(validCar.getId());

        // Assert
        assertFalse(result);
    }

    @Test
    void deleteAllCarsWithHostUserId_returnNothing() {
        //Arrange
        doNothing().when(carRepository).deleteAllByHostUserId(anyLong());

        //Act
        carService.deleteAllCarsWithHostUserId(validCar.getHostUserId());

        // Assert
        verify(carRepository).deleteAllByHostUserId(anyLong());
    }
}