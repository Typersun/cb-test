package savkin.ilya.service.impl;

import savkin.ilya.db.repository.ClientRepository;
import savkin.ilya.model.Client;
import savkin.ilya.service.ClientService;

import javax.enterprise.inject.Default;
import javax.inject.Inject;
import java.util.List;

@Default
public class ClientServiceImpl implements ClientService {
    @Inject
    private ClientRepository clientRepository;

    @Override
    public List<Client> findAllClients() {
        return clientRepository.findAll();
    }

    @Override
    public void saveClient(Client client) {
        clientRepository.save(client);
    }

    @Override
    public void updateClient(Client client) {
        clientRepository.update(client);
    }

    @Override
    public void deleteClient(Client client) {
        clientRepository.delete(client);
    }
}
