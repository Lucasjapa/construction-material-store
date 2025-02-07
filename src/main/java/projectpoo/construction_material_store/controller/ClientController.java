package projectpoo.construction_material_store.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import projectpoo.construction_material_store.domain.Client;
import projectpoo.construction_material_store.dto.ClientDTO;
import projectpoo.construction_material_store.service.ClientService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/clients")
// Define a classe como um controlador REST e mapeia as requisições para o endpoint "/clients"
public class ClientController {

    @Autowired
    private ClientService clientService;

    @PostMapping
    // Mapeia uma requisição POST para criar um novo produto
    public Client createClient(@RequestBody ClientDTO client) {
        // Chama o serviço para salvar o produto recebido no corpo da requisição
        return clientService.saveClient(client);
    }

    @GetMapping
    // Mapeia uma requisição GET para buscar todos os produtos
    public List<ClientDTO> getAllClients() {
        return clientService.getAllClients().stream()
                .map(ClientDTO::new)  // Converte cada produto para ProductDTO
                .collect(Collectors.toList());
    }

    @GetMapping("/search-clients/{cpfCnpj}")
    // Mapeia uma requisição GET para buscar os clientes que contenha aquele nome
    public List<ClientDTO> getClientByName(@PathVariable String cpfCnpj) {
        return clientService.getClientsByCpfCnpj(cpfCnpj).stream()
                .map(ClientDTO::new)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    // Mapeia uma requisição GET para buscar um cliente pelo seu ID
    public ResponseEntity<ClientDTO> getClientById(@PathVariable Long id) {
        Client client = clientService.getClientById(id);

        if (client != null) {
            return ResponseEntity.ok(new ClientDTO(client));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PutMapping("/updatecliente/{id}")
    // Mapeia uma requisição PUT para atualizar um produto pelo ID
    public ResponseEntity<Client> updateClient(@PathVariable Long id, @RequestBody ClientDTO clientDTO) {
        // Verifica se o produto existe
        Client existingClient = clientService.getClientById(id);

        if (existingClient != null) {
            // Salva o produto atualizado
            Client savedClient = clientService.updateClient(clientDTO, existingClient);
            return ResponseEntity.ok(savedClient);
        } else {
            // Retorna um status 404 caso o produto não seja encontrado
            return ResponseEntity.status(404).body(null);
        }
    }

    @DeleteMapping("/{id}")
    // Mapeia uma requisição DELETE para excluir um produto pelo ID
    public ResponseEntity<String> deleteClient(@PathVariable Long id) {
        // Verifica se o produto foi excluído com sucesso
        boolean isDeleted = clientService.deleteClientById(id);
        if (isDeleted) {
            return ResponseEntity.ok("Produto excluído com sucesso!");
        } else {
            // Retorna uma mensagem de erro caso o produto não seja encontrado
            return ResponseEntity.status(404).body("Produto não encontrado.");
        }
    }

    @DeleteMapping
    // Mapeia uma requisição DELETE para excluir múltiplos produtos com base em uma lista de IDs
    public ResponseEntity<String> deleteClients(@RequestBody List<Long> ids) {
        // Verifica se todos os produtos da lista foram excluídos com sucesso
        boolean allDeleted = clientService.deleteClientsByIds(ids);
        if (allDeleted) {
            return ResponseEntity.ok("Todos os produtos foram excluídos com sucesso!");
        } else {
            // Retorna um status 207 (Multi-Status) caso alguns produtos não tenham sido encontrados ou excluídos
            return ResponseEntity.status(207).body("Alguns produtos não foram encontrados ou não puderam ser excluídos.");
        }
    }
}
