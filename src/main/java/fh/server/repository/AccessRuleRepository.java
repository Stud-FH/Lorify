package fh.server.repository;

import fh.server.entity.access.AccessRule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("accessRuleRepository")
public interface AccessRuleRepository extends JpaRepository<AccessRule, String> {

}
