package fh.server.repository;

import fh.server.entity.login.PasswordLogin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository("passwordLoginRepository")
public interface PasswordLoginRepository extends JpaRepository<PasswordLogin, String> {

    Optional<PasswordLogin> findByIdentifier(String identifier);

    boolean existsByIdentifier(String identifier);
}
