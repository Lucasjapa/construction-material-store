package projectpoo.construction_material_store.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import projectpoo.construction_material_store.domain.Client;
import projectpoo.construction_material_store.domain.Invoice;
import projectpoo.construction_material_store.dto.ClientDTO;
import projectpoo.construction_material_store.dto.InvoiceDTO;
import projectpoo.construction_material_store.dto.ProductDTO;
import projectpoo.construction_material_store.service.ClientService;
import projectpoo.construction_material_store.service.InvoiceService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/invoices")
// Define a classe como um controlador REST e mapeia as requisições para o endpoint "/invoice"
public class InvoiceController {

    @Autowired
    private InvoiceService invoiceService;

    @PostMapping
    // Mapeia uma requisição POST para criar um novo produto
    public Invoice createInvoice(@RequestBody InvoiceDTO invoiceDTO) {
        // Chama o serviço para salvar o produto recebido no corpo da requisição
        return invoiceService.saveInvoice(invoiceDTO);
    }

    @GetMapping
    // Mapeia uma requisição GET para buscar todos os produtos
    public List<InvoiceDTO> getAllInvoices() {
        return invoiceService.getAllInvoices().stream()
                .map(InvoiceDTO::new)  // Converte cada produto para ProductDTO
                .collect(Collectors.toList());
    }

    @GetMapping("/search-invoices/{codInvoice}")
    // Mapeia uma requisição GET para buscar os invoice que contenha aquele nome
    public List<InvoiceDTO> getInvoiceByCodInvoice(@PathVariable String codInvoice) {
        return invoiceService.getInvoicesByCodInvoice(codInvoice).stream()
                .map(InvoiceDTO::new)
                .collect(Collectors.toList());
    }

    @DeleteMapping("/{id}")
    // Mapeia uma requisição DELETE para excluir um produto pelo ID
    public ResponseEntity<String> deleteInvoice(@PathVariable Long id) {
        // Verifica se o produto foi excluído com sucesso
        boolean isDeleted = invoiceService.deleteInvoiceById(id);
        if (isDeleted) {
            return ResponseEntity.ok("Produto excluído com sucesso!");
        } else {
            // Retorna uma mensagem de erro caso o produto não seja encontrado
            return ResponseEntity.status(404).body("Produto não encontrado.");
        }
    }

    @DeleteMapping
    // Mapeia uma requisição DELETE para excluir múltiplos produtos com base em uma lista de IDs
    public ResponseEntity<String> deleteInvoices(@RequestBody List<Long> ids) {
        // Verifica se todos os produtos da lista foram excluídos com sucesso
        boolean allDeleted = invoiceService.deleteInvoicesByIds(ids);
        if (allDeleted) {
            return ResponseEntity.ok("Todos os produtos foram excluídos com sucesso!");
        } else {
            // Retorna um status 207 (Multi-Status) caso alguns produtos não tenham sido encontrados ou excluídos
            return ResponseEntity.status(207).body("Alguns produtos não foram encontrados ou não puderam ser excluídos.");
        }
    }
}
