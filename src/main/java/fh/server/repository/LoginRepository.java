package fh.server.repository;

import fh.server.entity.login.Login;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("loginRepository")
public interface LoginRepository extends JpaRepository<Login, String> {

}
