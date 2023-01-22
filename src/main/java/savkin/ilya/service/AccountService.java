package savkin.ilya.service;

import savkin.ilya.model.Account;
import savkin.ilya.model.Client;

import java.util.List;
import java.util.Optional;

public interface AccountService {
    List<Account> findAllAccounts();
    Optional<Account> saveAccount(Account account, Client client);
}
