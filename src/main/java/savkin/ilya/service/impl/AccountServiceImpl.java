package savkin.ilya.service.impl;

import savkin.ilya.db.repository.AccountRepository;
import savkin.ilya.model.Account;
import savkin.ilya.model.Client;
import savkin.ilya.service.AccountService;

import javax.enterprise.inject.Default;
import javax.inject.Inject;
import java.util.List;
import java.util.Optional;

@Default
public class AccountServiceImpl implements AccountService {
    @Inject
    private AccountRepository accountRepository;


    @Override
    public List<Account> findAllAccounts() {
        return accountRepository.findAll();
    }

    @Override
    public Optional<Account> saveAccount(Account account, Client client) {
        account.setClientFullName(client.getFullName());
        account.setClientId(client.getId());
        return accountRepository.save(account);
    }
}
