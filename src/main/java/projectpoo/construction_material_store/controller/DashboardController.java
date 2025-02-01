package projectpoo.construction_material_store.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import projectpoo.construction_material_store.domain.Invoice;
import projectpoo.construction_material_store.dto.InvoiceDTO;
import projectpoo.construction_material_store.service.InvoiceItemService;
import projectpoo.construction_material_store.service.InvoiceService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/dashboard")
public class DashboardController {

    @Autowired
    private InvoiceService invoiceService;

    @Autowired
    private InvoiceItemService invoiceItemService;

    // Endpoint para retornar o total de faturas do mês atual
    @GetMapping("/total-current-month")
    public ResponseEntity<Long> getTotalInvoicesForCurrentMonth() {
        long totalInvoices = invoiceService.getTotalInvoicesForCurrentMonth();
        return ResponseEntity.ok(totalInvoices);
    }

    // Endpoint para buscar os 3 produtos mais vendidos
    @GetMapping("/top3")
    public ResponseEntity<List<Object[]>> getTop3MostFrequentProducts() {
        List<Object[]> topProducts = invoiceItemService.getTop3MostFrequentProducts();
        return ResponseEntity.ok(topProducts);
    }
}
