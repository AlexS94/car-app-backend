package de.fakultaet73.galvanize.carapp.api.carappapi.dataLoader;

import de.fakultaet73.galvanize.carapp.api.carappapi.documents.Booking;
import de.fakultaet73.galvanize.carapp.api.carappapi.documents.Car;
import de.fakultaet73.galvanize.carapp.api.carappapi.documents.ImageFile;
import de.fakultaet73.galvanize.carapp.api.carappapi.documents.User;
import de.fakultaet73.galvanize.carapp.api.carappapi.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import java.io.IOException;

@AllArgsConstructor
@Component
public class DataLoader implements ApplicationRunner {

    MongoTemplate mongoTemplate;
    UserRepository userRepository;
    UserDataLoader userDataLoader;
    CarDataLoader carDataLoader;
    BookingDataLoader bookingDataLoader;
    ImageFileDataLoader imageFileDataLoader;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        if (args.containsOption("loadData")) {
            resetDatabase();
            initializeData();
        }
    }

    private void resetDatabase() {
        mongoTemplate.getDb().drop();
    }

    private void initializeData() throws IOException {
        mongoTemplate.insert(userDataLoader.getUsers(), User.class);
        mongoTemplate.insert(carDataLoader.getCars(), Car.class);
        mongoTemplate.insert(bookingDataLoader.getBookings(), Booking.class);
        mongoTemplate.insert(imageFileDataLoader.getImageFiles(), ImageFile.class);
    }

}