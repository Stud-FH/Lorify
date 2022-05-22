package fh.server.repository;

import fh.server.entity.Artifact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("artifactRepository")
public interface ArtifactRepository extends JpaRepository<Artifact, String> {

}
