package fh.server.repository;

import fh.server.entity.Submission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("submissionRepository")
public interface SubmissionRepository extends JpaRepository<Submission, String> {

}
