package fh.server.service;

import fh.server.constant.Permission;
import fh.server.context.Principal;
import fh.server.entity.*;
import fh.server.repository.*;
import fh.server.rest.dao.PollDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class PollService extends ComponentService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PollService.class);


    protected final PollRepository pollRepository;
    protected final SubmissionRepository submissionRepository;
    private final AliasRepository aliasRepository;


    public PollService(
            @Qualifier("entityRepository") EntityRepository entityRepository,
            @Qualifier("resourceRepository") ResourceRepository resourceRepository,
            @Qualifier("dataRepository") DataRepository dataRepository,
            @Qualifier("componentRepository") ComponentRepository componentRepository,
            @Qualifier("pollRepository") PollRepository pollRepository,
            @Qualifier("submissionRepository") SubmissionRepository submissionRepository,
            @Qualifier("aliasRepository") AliasRepository aliasRepository

    ) {
        super(entityRepository, resourceRepository, dataRepository, componentRepository);
        this.pollRepository = pollRepository;
        this.submissionRepository = submissionRepository;
        this.aliasRepository = aliasRepository;
    }



    public Poll fetchPoll(String path) {
        return pollRepository.findByPath(path)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "poll not found"));
    }

    public Poll create(Scope scope, PollDAO dao, Account principal) {
        dao.setScope(scope);
        Poll created = (Poll) super.create(dao, principal, Permission.AuthorAccess, new Poll());
        return saveAndFlush(created);
    }

    public Poll update(Poll victim, PollDAO dao, Principal principal) {
        principal.requireAlias(victim);
        init(victim, dao, principal.getAlias(victim));
        victim = (Poll) super.update(victim, dao, principal);
        flush();
        return victim;
    }

    protected void init(Poll victim, PollDAO dao, Alias principal) {
        dao.setPrincipal(principal);
        if (dao.getSubmissionString() != null) {
            Submission submission = new Submission();
            submission.setString(dao.getSubmissionString());
            submission.setPoll(victim);
            submission.setAlias(principal);
            dao.setSubmission(submissionRepository.saveAndFlush(submission));
        }
    }

    protected Poll saveAndFlush(Poll created) {
        super.saveAndFlush(created);
        return pollRepository.saveAndFlush(created);
    }

    protected void flush() {
        super.flush();
        submissionRepository.flush();
        pollRepository.flush();
        aliasRepository.flush(); // may be affected by submission changes
    }

    protected String typeLabel() {
        return "poll";
    }

}
