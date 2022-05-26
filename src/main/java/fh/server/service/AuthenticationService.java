package fh.server.service;

import fh.server.entity.Account;
import fh.server.entity.Alias;
import fh.server.entity.Entity;
import fh.server.entity.Site;
import fh.server.repository.AccountRepository;
import fh.server.repository.EntityRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AuthenticationService extends EntityService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationService.class);



    private final AccountRepository accountRepository;

    public AuthenticationService(
            @Qualifier("entityRepository") EntityRepository entityRepository,
            @Qualifier("accountRepository") AccountRepository accountRepository
    ) {
        super(entityRepository);
        this.accountRepository = accountRepository;
    }

    public Entity principal(Site site, String token) {
        checkNotEmpty(token, "Authorization header");
        switch (token.charAt(0)) {
            case 'A': return fetchAccountByToken(token);
            case 'a': return fetchAliasByToken(site, token);
            default: throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "invalid token");
        }
    }

    public Account principalAsAccount(String token) {
        checkNotEmpty(token, "Authorization header");
        if (token.charAt(0) != 'A')
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "invalid token");
        return fetchAccountByToken(token);
    }

    public Alias principalAsAlias(Site site, String token) {
        checkNotEmpty(token, "Authorization header");
        if (token.charAt(0) != 'a')
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "invalid token");
        return fetchAliasByToken(site, token);
    }

    public Account fetchById(String id) {
        return accountRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "id not found"));
    }

    public Account fetchAccountByToken(String token) {
        return accountRepository.findByToken(token)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "invalid token"));
    }

    public Alias fetchAliasByToken(Site site, String token) {
        Alias alias = site.getAlias(token);
        if (alias != null) return alias;
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "invalid token");
    }

}
