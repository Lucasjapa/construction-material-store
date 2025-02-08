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

    @GetMapping("/search-purchases/{date}")
    // Mapeia uma requisição GET para buscar os invoice que contenha aquele nome
    public List<PurchaseDTO> getInvoiceByDate(@PathVariable String date) {
        return purchaseService.getPurchaseByDate(date).stream()
                .map(PurchaseDTO::new)
                .collect(Collectors.toList());
    }

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
}
