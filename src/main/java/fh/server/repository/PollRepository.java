package fh.server.repository;

import fh.server.entity.Poll;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository("pollRepository")
public interface PollRepository extends JpaRepository<Poll, String> {
    Optional<Poll> findByPath(String path);
    boolean existsByPath(String path);

}
