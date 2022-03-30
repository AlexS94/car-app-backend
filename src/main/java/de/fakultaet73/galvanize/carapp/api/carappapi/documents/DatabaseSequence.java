package de.fakultaet73.galvanize.carapp.api.carappapi.documents;

import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "database_sequences")
public class DatabaseSequence {

    @Id
    private String id;

    @Getter
    private long seq;

    public DatabaseSequence() {}

}