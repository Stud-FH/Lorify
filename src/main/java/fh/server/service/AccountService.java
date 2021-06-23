package fh.server.service;

import fh.server.entity.Account;
import fh.server.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AccountService {

    private final AccountRepository accountRepository;

    public AccountService(
            @Qualifier("accountRepository") AccountRepository accountRepository
    ) {
        this.accountRepository = accountRepository;
    }

    public Account verifyAccount(Long id, String token) {
        // TODO
        return null;
    }

    public Account createAccount(Account blueprint) {
        checkNameConstraints(blueprint.getName());
        checkNameUniqueness(blueprint.getName());
        checkAddressConstraints(blueprint.getAddress());
        checkPhoneNumberConstraints(blueprint.getPhoneNumber());

        // TODO
        return null;
    }

    public Account updateAccount(Long id, Account blueprint) {
        // TODO
        return null;
    }

    public void deleteAccount(Long id) {
        // TODO
    }



    private void checkNameConstraints(String name) {
        if (name == null || name.isEmpty())
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "name cannot be empty");
        if (!Character.isLetter(name.charAt(0)))
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "name must start with a letter");
        for (char c : name.toCharArray()) {
            if (!Character.isLetterOrDigit(c))
                throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "name can only consist of letters and digits");
        }
    }

    private void checkNameUniqueness(String name) {
        if (accountRepository.existsByName(name))
            throw new ResponseStatusException(HttpStatus.CONFLICT, "name is already taken");
    }

    private void checkAddressConstraints(String address) {
        // TODO
    }

    private void checkPhoneNumberConstraints(String phoneNumber) {
        // TODO
    }

}