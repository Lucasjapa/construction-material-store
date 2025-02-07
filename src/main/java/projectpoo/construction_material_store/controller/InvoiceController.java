package projectpoo.construction_material_store.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
public class InvoiceController {

    @Autowired
    private InvoiceService invoiceService;

    @PostMapping
    public Invoice createInvoice(@RequestBody InvoiceDTO invoiceDTO) {
        return invoiceService.saveInvoice(invoiceDTO);
    }

    @GetMapping
    public List<InvoiceDTO> getAllInvoices() {
        return invoiceService.getAllInvoices().stream()
                .map(InvoiceDTO::new)
                .collect(Collectors.toList());
    }

    @GetMapping("/search-invoices/{codInvoice}")
    public List<InvoiceDTO> getInvoiceByCodInvoice(@PathVariable String codInvoice) {
        return invoiceService.getInvoicesByCodInvoice(codInvoice).stream()
                .map(InvoiceDTO::new)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<InvoiceDTO> getInvoiceById(@PathVariable Long id) {
        Invoice invoice = invoiceService.getInvoiceById(id);

        if (invoice != null) {
            return ResponseEntity.ok(new InvoiceDTO(invoice));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PutMapping("/update-invoice/{id}")
    public ResponseEntity<Invoice> updateInvoice(@PathVariable Long id, @RequestBody InvoiceDTO invoiceDTO) {
        Invoice existingInvoice = invoiceService.getInvoiceById(id);

        if (existingInvoice != null) {
            Invoice updatedInvoice = invoiceService.updateInvoice(id, invoiceDTO);
            return ResponseEntity.ok(updatedInvoice);
        } else {
            // Retorna um status 404 caso a fatura não seja encontrada
            return ResponseEntity.status(404).body(null);
        }
    }

    @GetMapping("/total-current-month")
    public ResponseEntity<Long> getTotalInvoicesForCurrentMonth() {
        long totalInvoices = invoiceService.getTotalInvoicesForCurrentMonth();
        return ResponseEntity.ok(totalInvoices);
    }
}
