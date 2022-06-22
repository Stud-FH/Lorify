package fh.server.service;

import fh.server.entity.Entity;
import fh.server.repository.EntityRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collection;

@Service
public class EntityService {

    private static final Logger LOGGER = LoggerFactory.getLogger(EntityService.class);


    protected final EntityRepository entityRepository;

    public EntityService(
            @Qualifier("entityRepository") EntityRepository entityRepository
    ) {
        this.entityRepository = entityRepository;
    }




    protected Entity fetchEntity(String id) {
        return entityRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("%s not found", typeLabel())));
    }

    public void requireIdAvailability(String id) {
        if (entityRepository.existsById(id))
            throw new ResponseStatusException(HttpStatus.CONFLICT, "id already exists: " +id);
    }

    public void requireEntityExistence(String id) {
        if (!entityRepository.existsById(id))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("entity#%s not found", id));
    }

    protected void checkNotNull(Object o) {
        if (o == null)
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "missing data");
    }

    protected void checkNotEmpty(String s, String key) {
        if (s == null || s.isEmpty())
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "missing data: "+key);
    }

    protected void checkNotEmpty(Collection<?> collection, String key) {
        if (collection == null || collection.isEmpty())
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "missing data: "+key);
    }

    protected Entity saveAndFlush(Entity created) {
        return entityRepository.saveAndFlush(created);
    }

    protected void flush() {
        entityRepository.flush();
    }

    protected void deleteAndFlush(Entity entity) {
        entityRepository.delete(entity);
        entityRepository.flush();
        // cascade delete?
        LOGGER.info(String.format("entity deleted: %s", entity));
    }

    protected String typeLabel() {
        return "entity";
    }

}
