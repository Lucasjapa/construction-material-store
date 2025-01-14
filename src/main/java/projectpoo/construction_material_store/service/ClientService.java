package projectpoo.construction_material_store.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import projectpoo.construction_material_store.domain.Client;
import projectpoo.construction_material_store.repository.ClientRepository;

import java.util.List;

@Service
public class ClientService {

    @Autowired
    private ClientRepository clientRepository;

    public Client saveClient(Client Client) {
        return clientRepository.save(Client);
    }

    public List<Client> getAllClients() {
        return clientRepository.findAll();
    }

    public List<Client> getClientsByName(String name) {
        return clientRepository.findByName(name);
    }

    public void deleteClient(Long id) {
        clientRepository.deleteById(id);
    }

    public boolean deleteClientById(Long id) {
        if (clientRepository.existsById(id)) {
            clientRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }

    public boolean deleteClientsByIds(List<Long> ids) {
        List<Client> ClientsToDelete = clientRepository.findAllById(ids);
        if (!ClientsToDelete.isEmpty()) {
            clientRepository.deleteAll(ClientsToDelete);
            return true;
        }
        return false;
    }
}

