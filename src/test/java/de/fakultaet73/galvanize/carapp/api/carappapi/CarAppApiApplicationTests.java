package de.fakultaet73.galvanize.carapp.api.carappapi;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.fakultaet73.galvanize.carapp.api.carappapi.documents.User;
import de.fakultaet73.galvanize.carapp.api.carappapi.dtos.UserDTO;
import de.fakultaet73.galvanize.carapp.api.carappapi.repositories.BookingRepository;
import de.fakultaet73.galvanize.carapp.api.carappapi.repositories.CarRepository;
import de.fakultaet73.galvanize.carapp.api.carappapi.repositories.UserRepository;
import de.fakultaet73.galvanize.carapp.api.carappapi.model.Address;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CarAppApiApplicationTests {

    @Autowired
    TestRestTemplate restTemplate;

    @Autowired
    UserRepository userRepository;

    @Autowired
    CarRepository carRepository;

    @Autowired
    BookingRepository bookingRepository;

    List<User> testUserList;

    ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        testUserList = new ArrayList<>();
        fillTestUsersList();
        userRepository.saveAll(testUserList);
    }

    private void fillTestUsersList() {

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
            testUserList.add(userTmp);
        }
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
    }

    @Test
    public void applicationStarts() {
        CarAppApiApplication.main(new String[]{});
    }

    @Test
    void checkDatabase() {
        // Arrange
        List<User> userFromDatabaseList;

        // Act
        userFromDatabaseList = userRepository.findAll();

        //Assert
        for (int i = 0; i < userFromDatabaseList.size(); i++) {
            User user = userFromDatabaseList.get(i);
            assertEquals(user.getUserName(), testUserList.get(i).getUserName());
        }
    }

    @Test
    void getUser_id_returnsUser(){
        // Act
        ResponseEntity<User> response = restTemplate.getForEntity("/user/1", User.class);

        //Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertEquals(testUserList.get(0).getUserName(), response.getBody().getUserName());
    }

    @Test
    void validate_with_Password_Email() {
        // Arrange
        String url = "/validate?input=" + testUserList.get(0).getEmail() + "&password=" + testUserList.get(0).getPassword();

        //Act
        ResponseEntity<User> response = restTemplate.getForEntity(url, User.class);

        //Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertEquals(testUserList.get(0).getUserName(), response.getBody().getUserName());
    }

    @Test
    void validate_with_Password_UserName() {
        // Arrange
        String url = "/validate?input=" + testUserList.get(0).getUserName() + "&password=" + testUserList.get(0).getPassword();

        //Act
        ResponseEntity<User> response = restTemplate.getForEntity(url, User.class);

        //Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertEquals(testUserList.get(0).getUserName(), response.getBody().getUserName());
    }

    @Test
    void add_User_User_returnsUser() throws Exception {
        // Arrange
        User user = User.builder()
                .firstName("Vorname")
                .lastName("Nachname")
                .userName("Nutzername")
                .email("email@22222222.de")
                .password("password")
                .dateOfBirth(LocalDate.of(1999, 1, 1))
                .address(new Address("Examplestreet", "12", "Berlin", 12345))
                .build();

        String json = mapper.writeValueAsString(user);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", MediaType.APPLICATION_JSON_VALUE);
        HttpEntity<User> request = new HttpEntity<>(user, headers);

        // Act
        ResponseEntity<User> response = restTemplate.postForEntity("/user", request, User.class);

        //Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
    }

    @Test
    void updateUser_returnsUpdateUser() throws Exception {
        // Arrange
        User expected = testUserList.get(0);

        UserDTO user = UserDTO.builder()
                .id(1)
                .firstName("David")
                .lastName("Muller")
                .userName("firefox")
                .email("muller@web.de")
                .password("password")
                .dateOfBirth(LocalDate.of(1999, 1, 1))
                .address(new Address("New Street", "13", "Hanover", 11223))
                .build();


        String json = mapper.writeValueAsString(user);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", MediaType.APPLICATION_JSON_VALUE);
        HttpEntity<UserDTO> request = new HttpEntity<>(user, headers);

        // Act
        ResponseEntity<UserDTO> response = restTemplate.exchange("/user", HttpMethod.PUT, request, UserDTO.class);

        //Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertEquals(response.getBody().getAddress().getCity(), user.getAddress().getCity());
        assertEquals(response.getBody().getAddress().getZip(), user.getAddress().getZip());
    }

    @Test
    void deleteUser_Id_returnsOk() {
        // Act
        ResponseEntity<User> getUserBeforeDeleteResponse = restTemplate.getForEntity("/user/1", User.class);
        ResponseEntity<Void> deleteUserResponse = restTemplate.exchange("/user/1", HttpMethod.DELETE, null, Void.class);
        ResponseEntity<User> getUserAfterDeleteResponse = restTemplate.getForEntity("/user/1", User.class);

        //Assert
        assertThat(getUserBeforeDeleteResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(getUserBeforeDeleteResponse.getBody()).isNotNull();
        assertThat(deleteUserResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(getUserAfterDeleteResponse.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void contextLoads() {

    }

}
