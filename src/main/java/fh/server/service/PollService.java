package fh.server.service;

import fh.server.entity.*;
import fh.server.entity.widget.Poll;
import fh.server.entity.widget.Widget;
import fh.server.helpers.Context;
import fh.server.repository.*;
import fh.server.rest.dto.PageBlueprint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class PollService extends ArtifactService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PollService.class);



    protected final PollRepository pollRepository;
    protected final AliasService aliasService;

    public PollService(
            @Qualifier("entityRepository") EntityRepository entityRepository,
            @Qualifier("artifactRepository") ArtifactRepository artifactRepository,
            @Qualifier("pollRepository") PollRepository pollRepository,
            @Qualifier("aliasService") AliasService aliasService

    ) {
        super(entityRepository, artifactRepository);
        this.pollRepository = pollRepository;
        this.aliasService = aliasService;
    }

    /**
     * @return  poll, principal, artifact
     */
    public Context buildContext(Poll poll, Account principal) {
        return Context.build()
                .principal(principal)
                .artifact(poll)
                .poll(poll)
                .dispatch();
    }

    /**
     * @return  poll, artifact, input, alias
     */
    public Context buildContext(Poll poll, String input, Alias alias) {
        return Context.build()
                .alias(alias)
                .input(input)
                .artifact(poll)
                .poll(poll)
                .dispatch();
    }



    public Poll fetchById(String id) {
        return pollRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "poll not found"));
    }

    public Poll setPollFormulation(Poll poll, String formulation, Account principal) {
        Context context = buildContext(poll, principal);
        checkOwner(context);
        checkNotEmpty(formulation, "formulation");

        String previous = poll.getFormulation();
        poll.setFormulation(formulation);
        pollRepository.flush();
        LOGGER.info(String.format("poll reformulated. [%s=%s, formulation=%s, previous=%s, principal=%s]", typeLabel(), poll, formulation, previous, principal));
        return poll;
    }

    public Poll setSubmissionGuardDescription(Poll poll, String description, Account principal) {
        Context context = buildContext(poll, principal);
        checkOwner(context);
        String previous = poll.getSubmissionGuardDescription();
        poll.setSubmissionGuardDescription(description);
        pollRepository.flush();
        LOGGER.info(String.format("submission guard description set. [%s=%s, description=%s, previous=%s, principal=%s]", typeLabel(), poll, description, previous, principal));
        return poll;
    }

    public Poll setInspectorGuardDescription(Poll poll, String description, Account principal) {
        Context context = buildContext(poll, principal);
        checkOwner(context);
        String previous = poll.getInspectorGuardDescription();
        poll.setInspectorGuardDescription(description);
        pollRepository.flush();
        LOGGER.info(String.format("inspector guard description set. [%s=%s, description=%s, previous=%s, principal=%s]", typeLabel(), poll, description, previous, principal));
        return poll;
    }

    public Poll putSubmission(Poll poll, String submission, Alias alias) {
        Context context = buildContext(poll, submission, alias);
        checkSubmission(context);

        String previous = poll.submit(alias, submission);
        pollRepository.flush();
        LOGGER.info(String.format("submitted. [%s=%s, submission=%s, previous=%s, alias=%s]", typeLabel(), poll, submission, previous, alias));
        return poll;
    }

    public Poll revokeSubmission(Poll poll, Alias alias) {
        String previous = poll.revokeSubmission(alias);
        pollRepository.flush();
        LOGGER.info(String.format("submission revoked. [%s=%s, previous=%s, alias=%s]", typeLabel(), poll, previous, alias));
        return poll;
    }

    protected Poll putQuantification(Poll poll, String key, Integer value, Account principal) {
        Context context = buildContext(poll, principal);
        checkOwner(context);
        checkNotEmpty(key, "key");
        Integer previous = poll.putQuantification(key, value);
        pollRepository.flush();
        LOGGER.info(String.format("quantification set. [%s=%s, key=%s, value=%d, previous=%d, principal=%s]", typeLabel(), poll, key, value, previous, principal));
        return poll;
    }

    protected Poll removeQuantification(Poll poll, String key, Account principal) {
        Context context = buildContext(poll, principal);
        checkOwner(context);
        checkNotEmpty(key, "key");
        Integer previous = poll.removeQuantification(key);
        pollRepository.flush();
        LOGGER.info(String.format("quantification removed. [%s=%s, key=%s, previous=%d, principal=%s]", typeLabel(), poll, key, previous, principal));
        return poll;
    }

    private void checkSubmission(Context context) {
        if (!context.getPoll().getSubmissionGuard().resolve(context))
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "submission denied");
    }

    private void checkInspector(Context context) {
        if (!context.getPoll().getInspectorGuard().resolve(context))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "access denied");
    }

    protected String typeLabel() {
        return "poll";
    }

}
