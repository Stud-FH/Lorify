package fh.server.repository;

import fh.server.entity.Component;
import fh.server.entity.Scope;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository("componentRepository")
public interface ComponentRepository extends JpaRepository<Component, String> {

    boolean existsByPath(String path);
    Optional<Component> findByPath(String path);

}
