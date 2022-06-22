package fh.server.repository;

import fh.server.entity.Data;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("dataRepository")
public interface DataRepository extends JpaRepository<Data, String> {

}
