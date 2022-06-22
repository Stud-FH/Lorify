package fh.server.service;

import fh.server.context.Principal;
import fh.server.entity.Account;
import fh.server.entity.Alias;
import fh.server.entity.Entity;
import fh.server.entity.Scope;
import fh.server.repository.AccountRepository;
import fh.server.repository.AliasRepository;
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
    private final AliasRepository aliasRepository;

    public AuthenticationService(
            @Qualifier("entityRepository") EntityRepository entityRepository,
            @Qualifier("accountRepository") AccountRepository accountRepository,
            @Qualifier("aliasRepository") AliasRepository aliasRepository

    ) {
        super(entityRepository);
        this.accountRepository = accountRepository;
        this.aliasRepository = aliasRepository;
    }

    public Principal principal(String token) {
        checkNotEmpty(token, "Authorization header");
        switch (token.charAt(0)) {
            case 'A': return accountRepository.findByToken(token)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "invalid token"));
            case 'a': return aliasRepository.findByToken(token)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "invalid token"));
            default: throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "invalid token");
        }
    }

    public Account account(String token) {
        checkNotEmpty(token, "Authorization header");
        if (token.charAt(0) == 'A') return accountRepository.findByToken(token)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "invalid token"));
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "invalid token");
    }

    public Alias alias(String token) {
        checkNotEmpty(token, "Authorization header");
        if (token.charAt(0) == 'a') return aliasRepository.findByToken(token)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "invalid token"));
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "invalid token");
    }

    public Account principalAsAccount(String token) {
        checkNotEmpty(token, "Authorization header");
        if (token.charAt(0) != 'A')
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "invalid token");
        return fetchAccountByToken(token);
    }

    public Alias principalAsAlias(Scope scope, String token) {
        checkNotEmpty(token, "Authorization header");
        if (token.charAt(0) != 'a')
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "invalid token");
        return fetchAliasByToken(scope, token);
    }

    public Account fetchById(String id) {
        return accountRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "id not found"));
    }

    public Account fetchAccountByToken(String token) {
        return accountRepository.findByToken(token)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "invalid token"));
    }

    public Alias fetchAliasByToken(Scope scope, String token) {
        Alias alias = scope.getAlias(token);
        if (alias != null) return alias;
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "invalid token");
    }

}
