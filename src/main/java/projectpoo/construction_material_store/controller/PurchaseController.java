package projectpoo.construction_material_store.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import projectpoo.construction_material_store.domain.Invoice;
import projectpoo.construction_material_store.domain.Purchase;
import projectpoo.construction_material_store.dto.InvoiceDTO;
import projectpoo.construction_material_store.dto.PurchaseDTO;
import projectpoo.construction_material_store.service.InvoiceService;
import projectpoo.construction_material_store.service.PurchaseService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/purchases")
public class PurchaseController {

    @Autowired
    private PurchaseService purchaseService;

    @PostMapping
    public Purchase createPurchase(@RequestBody PurchaseDTO purchaseDTO) {
        return purchaseService.savePurchase(purchaseDTO);
    }

    @GetMapping
    public List<PurchaseDTO> getAllPurchase() {
        return purchaseService.getAllPurchases().stream()
                .map(PurchaseDTO::new)
                .collect(Collectors.toList());
    }
//
//    @GetMapping("/search-invoices/{codInvoice}")
//    // Mapeia uma requisição GET para buscar os invoice que contenha aquele nome
//    public List<InvoiceDTO> getInvoiceByCodInvoice(@PathVariable String codInvoice) {
//        return invoiceService.getInvoicesByCodInvoice(codInvoice).stream()
//                .map(InvoiceDTO::new)
//                .collect(Collectors.toList());
//    }
//
    @GetMapping("/{id}")
    // Mapeia uma requisição GET para buscar um cliente pelo seu ID
    public ResponseEntity<PurchaseDTO> getPurchaseById(@PathVariable Long id) {
        Purchase purchase = purchaseService.getPurchaseById(id);

        if (purchase != null) {
            return ResponseEntity.ok(new PurchaseDTO(purchase));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
//
//    @PutMapping("/update-invoice/{id}")
//    public ResponseEntity<Invoice> updateInvoice(@PathVariable Long id, @RequestBody InvoiceDTO invoiceDTO) {
//        // Verifica se a fatura existe
//        Invoice existingInvoice = invoiceService.getInvoiceById(id);
//
//        if (existingInvoice != null) {
//            Invoice updatedInvoice = invoiceService.updateInvoice(id, invoiceDTO);
//            return ResponseEntity.ok(updatedInvoice);
//        } else {
//            // Retorna um status 404 caso a fatura não seja encontrada
//            return ResponseEntity.status(404).body(null);
//        }
//    }
//
//    // Endpoint para retornar o total de faturas do mês atual
//    @GetMapping("/total-current-month")
//    public ResponseEntity<Long> getTotalInvoicesForCurrentMonth() {
//        long totalInvoices = invoiceService.getTotalInvoicesForCurrentMonth();
//        return ResponseEntity.ok(totalInvoices);
//    }
}
