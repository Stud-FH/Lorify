package fh.server.repository;

import fh.server.entity.Content;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository("contentRepository")
public interface ContentRepository extends JpaRepository<Content, String> {

    boolean existsByPath(String path);
    Optional<Content> findByPath(String path);

}
