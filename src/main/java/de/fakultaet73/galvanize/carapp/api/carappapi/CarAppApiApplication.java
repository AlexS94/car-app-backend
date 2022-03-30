package de.fakultaet73.galvanize.carapp.api.carappapi;

import de.fakultaet73.galvanize.carapp.api.carappapi.documents.Booking;
import de.fakultaet73.galvanize.carapp.api.carappapi.documents.Car;
import de.fakultaet73.galvanize.carapp.api.carappapi.documents.DatabaseSequence;
import de.fakultaet73.galvanize.carapp.api.carappapi.documents.User;
import de.fakultaet73.galvanize.carapp.api.carappapi.repositories.BookingRepository;
import de.fakultaet73.galvanize.carapp.api.carappapi.repositories.CarRepository;
import de.fakultaet73.galvanize.carapp.api.carappapi.repositories.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.MongoOperations;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class CarAppApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(CarAppApiApplication.class, args);
    }

  /*  @Bean
    CommandLineRunner runner(
            MongoOperations mongoOperations,
            UserRepository userRepository,
            CarRepository carRepository,
            BookingRepository bookingRepository) {

        List<User> testUsersList = new ArrayList<>();
        List<Car> testCarsList = new ArrayList<>();
        List<Booking> testBookingsList = new ArrayList<>();

        String[] firstNameArray = {"David", "Alex", "Marcel", "Marian", "Manfred"};
        String[] userNameArray = {"firefox", "Ddge", "JohnStar", "LowBoy", "SmartDude"};
        String[] emailsArray = {"muller@web.de", "schulze@icloud.com", "thompson@mail.com", "oneil@yahoo.de", "meyer@aol.com"};
        String[] lastNamesArray = {"Muller", "Schulze", "Thompson", "ONeil", "Meyer"};
        List<Address> addressesList = List.of(new Address("Examplestreet", "12", "Berlin", 12345),
                new Address("Musterstraße", "4", "Munich", 54321),
                new Address("Lindenstrasse", "1", "Wolfsburg", 11111),
                new Address("Upstate", "335", "New York", 55555),
                new Address("Compton Blvd", "83", "Los Angeles", 45123));

        for (int i = 0; i < 5; i++) {
            User userTmp = User.builder()
                    .id(i + 1)
                    .firstName(firstNameArray[i])
                    .lastName(lastNamesArray[i])
                    .userName(userNameArray[i])
                    .email(emailsArray[i])
                    .password("password")
                    .dateOfBirth(LocalDate.of(1999 - i, i + 1, i + 1))
                    .address(addressesList.get(i))
                    .build();
            testUsersList.add(userTmp);

            Car carTmp = Car.builder()
                    .id(i + 5)
                    .hostUserId((long) (i + 1))
                    .make("Volkswagen")
                    .model("Golf")
                    .type("Hatchback")
                    .year(2013)
                    .details(new CarDetails("diesel", 4, 5, 115, "manual"))
                    .pricePerDay(15)
                    .address(new Address("Musterstreet", "12", "Berlin", 12345))
                    .build();
            testCarsList.add(carTmp);

            Booking bookingTmp = Booking.builder()
                    .id(i + 10)
                    .carId((long) i + 5)
                    .userId((long) i + 5)
                    .from(LocalDate.of(2022 - i, i + 1, i + 1))
                    .until(LocalDate.of(2022 - i, i + 2, i + 2))
                    .build();
            testBookingsList.add(bookingTmp);
        }

        return args -> {
            mongoOperations.dropCollection(DatabaseSequence.class);
            mongoOperations.dropCollection(User.class);
            mongoOperations.dropCollection(Car.class);
            mongoOperations.dropCollection(Booking.class);
            userRepository.insert(testUsersList);
            carRepository.insert(testCarsList);
            bookingRepository.insert(testBookingsList);
        };
    }
*/
}

