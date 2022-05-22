package fh.server.repository;

import fh.server.entity.Page;
import fh.server.entity.Site;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository("pageRepository")
public interface PageRepository extends JpaRepository<Page, String> {

    Optional<Page> findById(String id);
    Optional<Page> findBySiteIdAndName(String siteId, String name);

    boolean existsById(String id);
    boolean existsBySiteIdAndName(String siteId, String name);
}
