package fh.server.repository;

import fh.server.entity.widget.File;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("fileRepository")
public interface FileRepository extends JpaRepository<File, String> {

}
