package fh.server.repository;

import fh.server.entity.Entity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("entityRepository")
public interface EntityRepository extends JpaRepository<Entity, String> {

}
