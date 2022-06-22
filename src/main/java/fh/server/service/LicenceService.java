package fh.server.service;

import fh.server.constant.Permission;
import fh.server.context.Principal;
import fh.server.entity.*;
import fh.server.repository.*;
import fh.server.rest.dao.ComponentDAO;
import fh.server.rest.dao.LicenceDAO;
import fh.server.rest.dao.ResourceDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class LicenceService extends EntityService {

    private static final Logger LOGGER = LoggerFactory.getLogger(LicenceService.class);


    protected final LicenceRepository licenceRepository;
    private final AccountRepository accountRepository;

    public LicenceService(
            @Qualifier("entityRepository") EntityRepository entityRepository,
            @Qualifier("licenceRepository") LicenceRepository licenceRepository,
            @Qualifier("accountRepository") AccountRepository accountRepository
    ) {
        super(entityRepository);
        this.licenceRepository = licenceRepository;
        this.accountRepository = accountRepository;
    }

    public Licence fetchLicence(String activationCode) {
        return licenceRepository.findByActivationCode(activationCode)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "licence not found: "+activationCode));
    }


    public Set<Licence> create(LicenceDAO dao, Account principal) {
        principal.requireSuperAdmin();
        checkNotEmpty(dao.getPrivileges(), "privileges");
        checkNotNull(dao.getExpiration());

        if (dao.getOwnerIds() != null) {
            Set<Account> accounts = new HashSet<>();
            for (String id : dao.getOwnerIds()) {
                accounts.add(accountRepository.findById(id)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "account not found: "+id)));
            }
            for (Account account : accounts) {
                produce(dao).activate(account);
            }
            flush();
        }

        Set<Licence> freeLicences = new HashSet<>();
        if (dao.getFreeCount() != null) {
            for (int i = 0; i < dao.getFreeCount(); i++) freeLicences.add(produce(dao));
        }
        return freeLicences;
    }

    protected Licence produce(LicenceDAO dao) {
        Licence created = new Licence();
        created.addPrivileges(dao.getPrivileges());
        created.setExpiration(dao.getExpiration());
        return saveAndFlush(created);
    }

    public Licence activate(Licence licence, Account principal) {
        licence.activate(principal);
        flush();
        return licence;
    }



    protected Licence saveAndFlush(Licence created) {
        super.saveAndFlush(created);
        return licenceRepository.saveAndFlush(created);
    }

    protected void flush() {
        super.flush();
        licenceRepository.flush();
        accountRepository.flush();
    }

    protected String typeLabel() {
        return "licence";
    }

}
