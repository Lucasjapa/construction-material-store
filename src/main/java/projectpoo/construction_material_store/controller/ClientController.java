package projectpoo.construction_material_store.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import projectpoo.construction_material_store.domain.Client;
import projectpoo.construction_material_store.dto.ClientDTO;
import projectpoo.construction_material_store.dto.ProductDTO;
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
        return clientService.saveClient(client.toclient());
    }

    @GetMapping
    // Mapeia uma requisição GET para buscar todos os produtos
    public List<ClientDTO> getAllClients() {
        // Retorna a lista de todos os produtos disponíveis
        return clientService.getAllClients().stream()
                .map(ClientDTO::new)  // Converte cada produto para ProductDTO
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    // Mapeia uma requisição GET para buscar os clientes que contenha aquele nome
    public List<Client> getClientByName(@PathVariable String name) {
        // Retorna o produto correspondente ao ID fornecido, ou null caso não encontrado
        return clientService.getClientsByName(name);
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
