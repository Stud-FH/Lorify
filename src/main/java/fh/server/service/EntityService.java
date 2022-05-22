package fh.server.service;

import fh.server.entity.Account;
import fh.server.entity.Entity;
import fh.server.helpers.Context;
import fh.server.repository.EntityRepository;
import fh.server.rest.dto.AttributeBlueprint;
import fh.server.rest.dto.EntityBlueprint;
import org.apache.logging.log4j.util.TriConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collection;
import java.util.function.BiConsumer;

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

    /**
     * @return  principal
     */
    public Context buildContext(Account principal) {
        return Context.build()
                .principal(principal)
                .dispatch();
    }

    /**
     * @return  principal, entity
     */
    public Context buildContext(Entity entity, Account principal) {
        return Context.build()
                .principal(principal)
                .entity(entity)
                .dispatch();
    }

    protected void verifyOwnerUpdate(EntityBlueprint blueprint, Context context) {
        checkNotNull(context.getEntity());
        checkOwner(context);
    }

    protected void performOwnerUpdate(EntityBlueprint blueprint, Context context) {
        context.getEntity().addOwner(blueprint.getOwnerIds());
        entityRepository.flush();
        LOGGER.info(String.format("owner added: %s in %s", blueprint.getOwnerIds(), context));
    }

    protected void verifyAttributeUpdate(EntityBlueprint blueprint, Context context) {
        checkNotNull(context.getEntity());
        checkOwner(context);
    }

    protected void performAttributeUpdate(EntityBlueprint blueprint, Context context) {
        checkNotNull(context.getEntity());
        checkNotNull(context.getPrincipal());
        checkOwner(context);
        checkNotNull(blueprint.getAttributeKeys());
        checkNotNull(blueprint.getAttributes());

        StringBuilder update = new StringBuilder();
        for (String key : blueprint.getAttributeKeys()) {
            String value = blueprint.getAttributes().get(key);
            String previous = context.getEntity().putAttribute(key, value);
            update.append(String.format("%s\"%s\" -> \"%s\" (prev:\"%s\")", update.length() == 0 ? "{" : ", ", key, value, previous));
        }
        update.append("}");
        entityRepository.flush();
        LOGGER.info(String.format("attributes updated: %s in %s", update, context));
    }

    protected void update(EntityBlueprint blueprint, Context context, EntityService service) {
        checkNotNull(blueprint);
        checkNotEmpty(blueprint.getChangelist(), "changelist");
        service.verifyUpdate(blueprint, context, service);
        service.performUpdate(blueprint, context, service);
    }

    protected void verifyUpdate(EntityBlueprint blueprint, Context context, EntityService service) {
        checkNotNull(blueprint);
        checkNotEmpty(blueprint.getChangelist(), "changelist");

        if (blueprint.getChangelist().contains("owner")) {
            service.verifyOwnerUpdate(blueprint, context);
        }
        if (blueprint.getChangelist().contains("attrib")) {
            service.verifyAttributeUpdate(blueprint, context);
        }
    }

    protected void performUpdate(EntityBlueprint blueprint, Context context, EntityService service) {
        if (blueprint.getChangelist().contains("owner")) {
            service.performOwnerUpdate(blueprint, context);
        }
        if (blueprint.getChangelist().contains("attrib")) {
            service.performAttributeUpdate(blueprint, context);
        }
    }

    protected void require(boolean b) {
        if (!b)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "unmet condition");
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

    protected void checkOwner(Context context) {
        if (context.getPrincipal() == null || !context.getEntity().hasOwner(context.getPrincipal()))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "access denied");
    }

    protected Entity saveAndFlush(Entity created) {
        return entityRepository.saveAndFlush(created);
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
