package fh.server.repository;

import fh.server.entity.Licence;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository("licenceRepository")
public interface LicenceRepository extends JpaRepository<Licence, String> {

    Optional<Licence> findByActivationCode(String activationCode);
}
