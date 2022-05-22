package fh.server.repository;

import fh.server.entity.Site;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository("siteRepository")
public interface SiteRepository extends JpaRepository<Site, String> {
    Optional<Site> findById(String id);
    Optional<Site> findByName(String name);

    boolean existsByName(String name);
}
