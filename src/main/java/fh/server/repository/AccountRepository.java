package fh.server.repository;

import fh.server.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository("accountRepository")
public interface AccountRepository extends JpaRepository<Account, String> {

    Optional<Account> findById(String id);
    Optional<Account> findByToken(String token);

    boolean existsById(String id);
    boolean existsByToken(String token);
}
