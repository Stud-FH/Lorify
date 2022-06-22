package fh.server.repository;

import fh.server.entity.Scope;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository("scopeRepository")
public interface ScopeRepository extends JpaRepository<Scope, String> {
    Optional<Scope> findByPath(String path);
    boolean existsByPath(String path);
}
