package savkin.ilya.service;

import savkin.ilya.model.Client;

import java.util.List;

public interface ClientService {
    List<Client> findAllClients();

    void saveClient(Client client);

    void updateClient(Client client);

    void deleteClient(Client client);
}
