package fh.server.repository;

import fh.server.entity.Alias;
import fh.server.entity.Site;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository("aliasRepository")
public interface AliasRepository extends JpaRepository<Alias, String> {

    Optional<Alias> findById(String id);
    Optional<Alias> findBySiteIdAndName(String siteId, String name);

    boolean existsById(String id);
    boolean existsBySiteIdAndName(String siteId, String name);
}
