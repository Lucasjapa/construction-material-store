package projectpoo.construction_material_store.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import projectpoo.construction_material_store.domain.Invoice;
import projectpoo.construction_material_store.dto.DashboardDTO;
import projectpoo.construction_material_store.dto.InvoiceDTO;
import projectpoo.construction_material_store.dto.ProductDTO;
import projectpoo.construction_material_store.service.InvoiceItemService;
import projectpoo.construction_material_store.service.InvoiceService;
import projectpoo.construction_material_store.service.ProductService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/dashboard")
public class DashboardController {

    @Autowired
    private InvoiceService invoiceService;

    @Autowired
    private InvoiceItemService invoiceItemService;

    @Autowired
    private ProductService productService;

    @GetMapping
    public ResponseEntity<DashboardDTO> getDashboard() {
        List<Object[]> topMostProducts = invoiceItemService.getTop3MostFrequentProducts();
        List<Object[]> topLeastProducts = invoiceItemService.getTop3LeastFrequentProducts();
        List<ProductDTO> productDTOS = productService.getLowStockProducts().stream()
                .map(ProductDTO::new)  // Converte cada produto para ProductDTO
                .toList();
        DashboardDTO dashboardDTO = new DashboardDTO(topMostProducts, topLeastProducts, productDTOS, invoiceService.getTotalInvoicesForCurrentMonth());

        return ResponseEntity.ok(dashboardDTO);
    }

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
