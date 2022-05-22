package fh.server.service;

import fh.server.constant.LoginMethod;
import fh.server.entity.Account;
import fh.server.entity.Site;
import fh.server.entity.login.Login;
import fh.server.entity.login.PasswordLogin;
import fh.server.entity.login.TokenLogin;
import fh.server.helpers.Tokens;
import fh.server.repository.*;
import fh.server.rest.dto.AttributeBlueprint;
import fh.server.rest.dto.LoginBlueprint;
import fh.server.rest.dto.LoginDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.annotation.PostConstruct;
import java.util.UUID;

@Service
public class AccountService extends EntityService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AccountService.class);



    private final AccountRepository accountRepository;
    private final LoginRepository loginRepository;
    private final TokenLoginRepository tokenLoginRepository;
    private final PasswordLoginRepository passwordLoginRepository;

    public AccountService(
            @Qualifier("entityRepository") EntityRepository entityRepository,
            @Qualifier("accountRepository") AccountRepository accountRepository,
            @Qualifier("loginRepository") LoginRepository loginRepository,
            @Qualifier("tokenLoginRepository") TokenLoginRepository tokenLoginRepository,
            @Qualifier("passwordLoginRepository") PasswordLoginRepository passwordLoginRepository
    ) {
        super(entityRepository);
        this.accountRepository = accountRepository;
        this.loginRepository = loginRepository;
        this.tokenLoginRepository = tokenLoginRepository;
        this.passwordLoginRepository = passwordLoginRepository;
    }

    public Account fetchById(String id) {
        return accountRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "id not found"));
    }

    public Account fetchByToken(String token) {
        checkNotEmpty(token, "Authorization header");
        TokenLogin tokenLogin = tokenLoginRepository.findByToken(token)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "token not found"));
        return fetchById(tokenLogin.getAccountId());
    }

    public Login fetchLogin(String id) {
        return loginRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "login not found"));
    }

    public Account login(LoginBlueprint login) {
        switch(login.getLoginMethod()) {
            case Token: return fetchByToken(login.getToken());
            case Password: {
                PasswordLogin passwordLogin = passwordLoginRepository.findByIdentifier(login.getIdentifier())
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "wrong identity or password"));
                if (!passwordLogin.matches(login.getPassword()))
                    throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "wrong identity or password");
                return fetchById(passwordLogin.getAccountId());
            }
            case OAuth2:
            default: throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "unexpected login method");
        }
        // todo wait
    }

    public TokenLogin create(LoginBlueprint loginBlueprint) {
        checkLoginBlueprint(loginBlueprint); // check twice to avoid creating a zombie account
        Account account = accountRepository.saveAndFlush(new Account());
        LOGGER.info(String.format("account created: %s", account));
        addLogin(loginBlueprint, account);
        LoginBlueprint tokenBlueprint = new LoginBlueprint();
        tokenBlueprint.setLoginMethod(LoginMethod.Token);
        tokenBlueprint.setToken(UUID.randomUUID().toString());
        return (TokenLogin) addLogin(tokenBlueprint, account);
    }

    public Login addLogin(LoginBlueprint loginBlueprint, Account principal) {
        checkLoginBlueprint(loginBlueprint);

        Login login;
        switch(loginBlueprint.getLoginMethod()) {
            case Token:
                TokenLogin tokenLogin = new TokenLogin();
                tokenLogin.setAccountId(principal.getId());
                tokenLogin.setToken(loginBlueprint.getToken());
                loginRepository.saveAndFlush(tokenLogin);
                tokenLoginRepository.saveAndFlush(tokenLogin);
                login = tokenLogin;
                break;
            case Password:
                PasswordLogin passwordLogin = new PasswordLogin();
                passwordLogin.setAccountId(principal.getId());
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
        return login;
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
        if (tokenLoginRepository.existsByToken(token))
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

    private void checkLoginBlueprint(LoginBlueprint blueprint) {
        checkNotNull(blueprint);
        switch (blueprint.getLoginMethod()) {
            case Token:
                checkNotEmpty(blueprint.getToken(), "token");
                checkTokenUnique(blueprint.getToken());
                break;
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
