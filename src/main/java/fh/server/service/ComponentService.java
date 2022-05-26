package fh.server.service;

import fh.server.entity.*;
import fh.server.entity.widget.Poll;
import fh.server.helpers.Context;
import fh.server.helpers.Operation;
import fh.server.repository.*;
import fh.server.rest.dao.EntityDAO;
import fh.server.rest.dao.WidgetComponentDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class ComponentService extends EntityService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ComponentService.class);



    protected final PollRepository pollRepository;
    protected final AliasService aliasService;

    public ComponentService(
            @Qualifier("entityRepository") EntityRepository entityRepository,
            @Qualifier("pollRepository") PollRepository pollRepository,
            @Qualifier("aliasService") AliasService aliasService

    ) {
        super(entityRepository);
        this.pollRepository = pollRepository;
        this.aliasService = aliasService;
    }



    public Poll fetchById(String id) {
        return pollRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "poll not found"));
    }

    public Poll operate(Poll poll, WidgetComponentDAO dao, Entity principal) {
        super.operate(dao, new Context(principal, poll), this);
        return poll;
    }

    protected Operation setup(String opKey, EntityDAO dao, Context context) {
        WidgetComponentDAO componentDAO = (WidgetComponentDAO) dao;

        if ("formulation".equals(opKey)) {
            checkNotEmpty(componentDAO.getFormulation(), "formulation");
            return context.operation(opKey, componentDAO.getFormulation(), context.victimAsPoll().getFormulation());
        }

        if (opKey.startsWith("Poll.q:")) {
            String key = opKey.substring( "Poll.q:".length());
            return context.operation(opKey, componentDAO.getQuantification().get(key), context.victimAsPoll().getQuantification(key));
        }
        return super.setup(opKey, dao, context);
    }

    protected void execute(Operation operation, EntityDAO dao) {
        WidgetComponentDAO componentDAO = (WidgetComponentDAO) dao;
        String opKey = operation.getOperation();

        if ("formulation".equals(opKey)) {
            checkNotEmpty(componentDAO.getFormulation(), "formulation");
            operation.victimAsPoll().setFormulation(componentDAO.getFormulation()); return;
        }
        if (opKey.startsWith("Poll.q:")) {
            String key = opKey.substring( "Poll.q:".length());
            operation.victimAsPoll().putQuantification(key, operation.valueAsInteger());
            return;
        }
        super.execute(operation, dao);
    }

    public Poll putSubmission(Poll poll, String submission, Alias alias) {
        Operation operation = new Context(alias, poll).operation("submit", submission, poll.getSubmission(alias));
        verifyOperation(operation);
        poll.submit(operation.principalAsAlias(), operation.getValue());
        pollRepository.flush();
        LOGGER.info(operation.toString());
        return poll;
    }

    public Poll revokeSubmission(Poll poll, Alias alias) {
        Operation operation = new Context(alias, poll).operation("revoke-submission", null, poll.getSubmission(alias));
        verifyOperation(operation);
        poll.revokeSubmission(operation.principalAsAlias());
        pollRepository.flush();
        LOGGER.info(operation.toString());
        return poll;
    }

    protected String typeLabel() {
        return "poll";
    }

}
