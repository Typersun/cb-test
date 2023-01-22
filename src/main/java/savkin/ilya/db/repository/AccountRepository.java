package savkin.ilya.db.repository;

import savkin.ilya.model.Account;

import java.util.List;
import java.util.Optional;

public interface AccountRepository {
    Optional<Account> save(Account account);
    Optional<Account> update(Account account);
    void delete(Account account);
    List<Account> findAll();
}
