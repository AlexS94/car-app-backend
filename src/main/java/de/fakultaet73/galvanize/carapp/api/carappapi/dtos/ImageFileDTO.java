package de.fakultaet73.galvanize.carapp.api.carappapi.dtos;

import lombok.*;
import org.bson.types.Binary;
import org.springframework.data.annotation.Id;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class ImageFileDTO {

    @Id
    private long id;

    private String contentType;

    private Binary content;
}
