package fh.server.repository;

import fh.server.entity.widget.Poll;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("pollRepository")
public interface PollRepository extends JpaRepository<Poll, String> {

}
