package fh.server.repository;

import fh.server.entity.Alias;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository("aliasRepository")
public interface AliasRepository extends JpaRepository<Alias, String> {

    Optional<Alias> findById(String id);
    Optional<Alias> findByParentIdAndName(String siteId, String name);

    boolean existsById(String id);
    boolean existsByParentIdAndName(String siteId, String name);
}
