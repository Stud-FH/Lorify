package fh.server.rest.dao;

import fh.server.entity.Account;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AliasDAO extends ResourceDAO {

    String accountId;
    Account account;
    String token;




    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
