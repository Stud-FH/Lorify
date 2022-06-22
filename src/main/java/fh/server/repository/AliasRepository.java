package fh.server.repository;

import fh.server.entity.Alias;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository("aliasRepository")
public interface AliasRepository extends JpaRepository<Alias, String> {

    Optional<Alias> findByPath(String path);
    Optional<Alias> findByToken(String token);

    boolean existsByPath(String path);
    boolean existsByToken(String token);
}
