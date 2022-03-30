package de.fakultaet73.galvanize.carapp.api.carappapi.documents;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import de.fakultaet73.galvanize.carapp.api.carappapi.ReferenceType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.Binary;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Builder
@Document
public class ImageFile {

    @Transient
    public static final String SEQUENCE_NAME = "imageFiles_sequence";

    @Id
    @Setter
    @Getter
    private long id;

    @NotNull
    @Getter
    private Long referenceId;

    @NotNull
    @Getter
    private ReferenceType type;
    private String name;

    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd")
    @NotNull
    private LocalDate createdTime;

    @NotNull
    private Binary content;

    private String contentType;
    private long size;

}
