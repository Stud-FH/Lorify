package fh.server.rest.dto;

public class PasswordLoginDTO extends LoginDTO {

    private String identifier;




    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }
}
