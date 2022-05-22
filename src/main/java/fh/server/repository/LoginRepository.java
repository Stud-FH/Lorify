package fh.server.repository;

import fh.server.entity.login.Login;
import fh.server.entity.login.TokenLogin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository("loginRepository")
public interface LoginRepository extends JpaRepository<Login, String> {

}
