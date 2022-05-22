package fh.server.rest.dto;

public class TokenLoginDTO extends LoginDTO {

    private String token;


    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
