package fh.server.service;

import fh.server.entity.Account;
import fh.server.entity.login.Login;
import fh.server.entity.login.PasswordLogin;
import fh.server.repository.*;
import fh.server.rest.dao.LoginDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AccountService extends EntityService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AccountService.class);



    private final AccountRepository accountRepository;
    private final LoginRepository loginRepository;
    private final PasswordLoginRepository passwordLoginRepository;

    public AccountService(
            @Qualifier("entityRepository") EntityRepository entityRepository,
            @Qualifier("accountRepository") AccountRepository accountRepository,
            @Qualifier("loginRepository") LoginRepository loginRepository,
            @Qualifier("passwordLoginRepository") PasswordLoginRepository passwordLoginRepository
    ) {
        super(entityRepository);
        this.accountRepository = accountRepository;
        this.loginRepository = loginRepository;
        this.passwordLoginRepository = passwordLoginRepository;
    }


    public boolean existsById(String id) {
        return accountRepository.existsById(id);
    }

    public boolean existsByIdentifier(String identifier) {
        return passwordLoginRepository.existsByIdentifier(identifier);
    }

    public Account fetchById(String id) {
        return accountRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "id not found"));
    }

    public Account fetchByToken(String token) {
        checkNotEmpty(token, "Authorization header");
        return accountRepository.findByToken(token)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "token not found"));
    }

    public Login fetchLogin(String id) {
        return loginRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "login not found"));
    }

    public Account login(LoginDAO login) throws InterruptedException {
        switch(login.getLoginMethod()) {
            case Password: {
                long targetTime = System.currentTimeMillis() +1000;

                PasswordLogin passwordLogin = passwordLoginRepository.findByIdentifier(login.getIdentifier())
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "wrong identity or password"));
                if (!passwordLogin.matches(login.getPassword()))
                    throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "wrong identity or password");
                Account account = fetchById(passwordLogin.getOwnerId());
                long waitingTime = targetTime - System.currentTimeMillis();
                LOGGER.info("waiting "+waitingTime+"ms before answering login request");
                if (waitingTime > 0) Thread.sleep(waitingTime);
                return account;
            }
            case OAuth2:
            default: throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "unexpected login method");
        }
    }

    public Account create(LoginDAO dao) {
        checkLoginBlueprint(dao); // check twice to avoid creating a zombie account
        Account account = new Account();
        account.setOwnerId(account.getId());
        account = accountRepository.saveAndFlush(account);
        LOGGER.info(String.format("account created: %s", account));
        return addLogin(dao, account);
    }

    public Account addLogin(LoginDAO loginBlueprint, Account principal) {
        checkLoginBlueprint(loginBlueprint);

        Login login;
        switch(loginBlueprint.getLoginMethod()) {
            case Password:
                PasswordLogin passwordLogin = new PasswordLogin();
                passwordLogin.setOwner(principal);
                passwordLogin.setIdentifier(loginBlueprint.getIdentifier());
                passwordLogin.setPassword(loginBlueprint.getPassword());
                loginRepository.saveAndFlush(passwordLogin);
                passwordLoginRepository.saveAndFlush(passwordLogin);
                login = passwordLogin;
                break;
            case OAuth2:
            default: throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "unexpected login method");
        }
        principal.addLogin(login);
        accountRepository.flush();
        LOGGER.info(String.format("account login added: %s <- %s", principal, login));
        return principal;
    }

    public Account removeLogin(String loginId, Account principal) {
        Login login = fetchLogin(loginId);
        checkOwnsLogin(login, principal);
        checkLoginRedundancy(principal);
        principal.removeLogin(login);
        accountRepository.flush();
        LOGGER.info(String.format("account login removed: %s - %s", principal, login));
        loginRepository.delete(login);
        loginRepository.flush();
        return principal;
    }

    private void checkTokenUnique(String token) {
        if (accountRepository.existsByToken(token))
            throw new ResponseStatusException(HttpStatus.CONFLICT, "token collision"); // TODO security gap
    }

    private void checkIdentifierUnique(String identifier) {
        if (passwordLoginRepository.existsByIdentifier(identifier))
            throw new ResponseStatusException(HttpStatus.CONFLICT, "identifier collision");
    }

    private void checkPasswordSecure(String password) {
        // TODO add more
        if (password.length() < 8)
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "password too short");
    }

    private void checkLoginRedundancy(Account account) {
        if (account.getLogins().size() < 2)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "requires a second login");
    }

    private void checkOwnsLogin(Login login, Account account) {
        if (!account.getLogins().contains(login))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "missing rights on this login");
    }

    private void checkLoginBlueprint(LoginDAO blueprint) {
        checkNotNull(blueprint);
        switch (blueprint.getLoginMethod()) {
            case Password:
                checkNotEmpty(blueprint.getIdentifier(), "identifier");
                checkIdentifierUnique(blueprint.getIdentifier());
                checkNotEmpty(blueprint.getPassword(), "password");
                checkPasswordSecure(blueprint.getPassword());
                break;
            case OAuth2:
                // TODO
                break;
            default: throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "missing data");

        }
    }

}
