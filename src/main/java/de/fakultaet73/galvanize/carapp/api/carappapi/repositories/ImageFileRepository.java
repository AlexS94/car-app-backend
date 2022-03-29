package de.fakultaet73.galvanize.carapp.api.carappapi.repositories;

import de.fakultaet73.galvanize.carapp.api.carappapi.ReferenceType;
import de.fakultaet73.galvanize.carapp.api.carappapi.documents.ImageFile;
import org.bson.types.Binary;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ImageFileRepository extends MongoRepository<ImageFile, Long> {

    boolean existsImageFileByReferenceIdAndType(long referenceId, ReferenceType type);

    boolean existsImageFileById(long id);

    void deleteAllByReferenceIdAndType(long referenceId, ReferenceType type);

    List<ImageFile> findAllByReferenceIdAndType(long referenceId, ReferenceType type);

    ImageFile findFirstByReferenceIdAndType(long referenceId, ReferenceType type);
}
