package de.fakultaet73.galvanize.carapp.api.carappapi.documents;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import de.fakultaet73.galvanize.carapp.api.carappapi.Address;
import de.fakultaet73.galvanize.carapp.api.carappapi.Rating;
import lombok.*;
import org.bson.types.Binary;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@Getter
@Builder
@Document
public class User {

    @Transient
    public static final String SEQUENCE_NAME = "users_sequence";

    @Id
    @Setter
    private long id;

    @NotNull
    private String firstName;
    @NotNull
    private String lastName;
    @NotNull @Indexed(unique = true)
    private String userName;
    @NotNull @Email @Indexed(unique = true)
    private String email;
    @NotNull
    private String password;


    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd")
    @NotNull
    private LocalDate dateOfBirth;

    private Binary image;

    @NotNull @Valid
    private Address address;
    private List<Rating> ratings;
    private int[] cars;
    private int[] bookedCars;



}
