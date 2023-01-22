package savkin.ilya.db.repository;

import savkin.ilya.model.Client;

import java.util.List;
import java.util.Optional;

public interface ClientRepository {
    List<Client> findAll();

    Optional<Client> save(Client client);

    Optional<Client> update(Client client);

    void delete(Client client);
}
