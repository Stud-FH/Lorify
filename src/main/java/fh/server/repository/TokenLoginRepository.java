package fh.server.repository;

import fh.server.entity.login.TokenLogin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository("tokenLoginRepository")
public interface TokenLoginRepository extends JpaRepository<TokenLogin, String> {

    Optional<TokenLogin> findByToken(String token);

    boolean existsByToken(String token);
}
