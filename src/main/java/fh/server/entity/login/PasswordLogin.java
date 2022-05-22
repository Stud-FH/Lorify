package fh.server.entity.login;

import fh.server.constant.LoginMethod;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.Column;


@javax.persistence.Entity
public class PasswordLogin extends Login {

    private static PasswordEncoder passwordEncoder;

    @Column(unique = true, nullable = false)
    private String identifier;

    @Column(nullable = false)
    private String cipher;




    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
        setLastModified(System.currentTimeMillis());
    }

    public String getCipher() {
        return cipher;
    }

    public void setPassword(String password) {
        this.cipher = getPasswordEncoder().encode(password);
        setLastModified(System.currentTimeMillis());
    }


    protected static PasswordEncoder getPasswordEncoder() {
        if (passwordEncoder == null) {
            passwordEncoder = new BCryptPasswordEncoder(16);
        }
        return passwordEncoder;
    }

    public boolean matches (String password) {
        return getPasswordEncoder().matches(password, cipher);
    }

    @Override
    public LoginMethod getLoginMethod() {
        return LoginMethod.Password;
    }
}
