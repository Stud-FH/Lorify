package fh.server.repository;

import fh.server.entity.Resource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository("resourceRepository")
public interface ResourceRepository extends JpaRepository<Resource, String> {

    boolean existsByPath(String path);
    Optional<Resource> findByPath(String path);

}
