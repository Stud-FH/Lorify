package fh.server.service;

import fh.server.constant.EntityType;
import fh.server.constant.TrustLevel;
import fh.server.helpers.Operation;
import fh.server.entity.Entity;
import fh.server.helpers.Context;
import fh.server.repository.EntityRepository;
import fh.server.rest.dao.EntityDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

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

    protected void operate(EntityDAO dao, Context context, EntityService service) {
        checkNotNull(dao);
        checkNotEmpty(dao.getOreationKeys(), "operation keys");

        // SETUP
        List<Operation> operations;
        try {
            operations = dao.getOreationKeys().stream().map(k -> setup(k, dao, context)).collect(Collectors.toList());
        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            // todo differentiate exceptions
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "incomplete data");
        }

        // VERIFICATION
        if (!context.isOwnerAccess()) {
            for (Operation operation : operations) {
                verifyOperation(operation);
            }
        }

        // EXECUTION
        for (Operation operation : operations) {
            service.execute(operation, dao);
            LOGGER.info(operation.toString());
        }

        service.flush();
    }

    protected Operation setup(String opKey, EntityDAO dao, Context context) {

        switch (opKey) {
            case "get": return context.operation(opKey, null, null);
            case "claim-entity": {
                if (context.principalType() != EntityType.Account)
                    throw new ResponseStatusException(HttpStatus.FORBIDDEN, "must use an account to claim an entity");

                return context.operation(opKey, dao.getOwnerId(), context.getVictim().getOwnerId());
            }
        }

        String victimType = context.victimType().toString();
        if (opKey.startsWith(victimType +".")) {
            // e.g. Alias.a:address
            String key = opKey.substring( victimType.length() +3);
            // e.g. address
            switch (opKey.charAt(victimType.length() +1)) {
                case 'a': return context.operation(opKey, dao.getAttributes().get(key), context.getVictim().getAttribute(key));
                case 'r': return context.operation(opKey, dao.getAccessRequirements().get(key), context.getVictim().getAccessRequirement(key));
                case 'g': return context.operation(opKey, dao.getGuards().get(key), context.getVictim().getGuard(key));
            }
        }
        throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "unexpected operation key: "+opKey);
    }

    protected void execute(Operation operation, EntityDAO dao) {

        String opKey = operation.getOperation();

        switch (opKey) {
            case "get": return;
            case "claim-entity": operation.getVictim().setOwner(operation.principalAsAccount()); return;
        }
        int dotIndex = operation.victimType().toString().length();
        String key = opKey.substring( dotIndex +3);
        switch (operation.getOperation().charAt(dotIndex +1)) {
            case 'a': operation.getVictim().putAttribute(key, operation.getValue()); return;
            case 'r': operation.getVictim().putAccessRequirements(key, TrustLevel.valueOf(operation.getValue().toString())); return;
            case 'g': operation.getVictim().putGuard(key, operation.getValue()); return;
        }
    }

    protected void verifyOperation(Operation operation) {
        if (operation.isOwnerAccess()) return;
        TrustLevel accessRequirement = operation.getVictim().getAccessRequirement(operation.getOperation());
        TrustLevel clearance = operation.getVictim().getDecodedGuard(operation.getOperation()).authenticate(operation);
        if (clearance == null) clearance = TrustLevel.Viewer;
        if (!clearance.meets(accessRequirement))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "access denied: " +operation);
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
