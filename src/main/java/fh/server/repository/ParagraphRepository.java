package fh.server.repository;

import fh.server.entity.widget.Paragraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("paragraphRepository")
public interface ParagraphRepository extends JpaRepository<Paragraph, String> {

}
