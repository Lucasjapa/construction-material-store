package projectpoo.construction_material_store.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import projectpoo.construction_material_store.domain.Client;
import projectpoo.construction_material_store.domain.Invoice;
import projectpoo.construction_material_store.domain.InvoiceItem;
import projectpoo.construction_material_store.domain.Product;
import projectpoo.construction_material_store.dto.InvoiceDTO;
import projectpoo.construction_material_store.dto.InvoiceItemDTO;
import projectpoo.construction_material_store.repository.InvoiceRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class InvoiceService {

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Autowired
    private ClientService clientService;

    @Autowired
    private ProductService productService;

    public Invoice saveInvoice(InvoiceDTO invoiceDTO) {
        Client client = clientService.getClientById(invoiceDTO.getClient().getId());

        List<InvoiceItem> invoiceItems = new ArrayList<>();

        for (InvoiceItemDTO invoiceItemDTO : invoiceDTO.getInvoiceItens()) {
            Product product = productService.getProductById(invoiceItemDTO.getProduct().getId());
            invoiceItems.add(new InvoiceItem(product, invoiceItemDTO.getQuantitySale(), invoiceItemDTO.getUnitPrice()));
        }

        return invoiceRepository.save(invoiceDTO.toInvoice(client, invoiceItems));
    }

    public Invoice updateInvoice(Long id, InvoiceDTO invoiceDTO) {
        Invoice existingInvoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invoice not found"));

        existingInvoice.setStatus(Invoice.Status.valueOf(invoiceDTO.getStatus()));

        return invoiceRepository.save(existingInvoice);
    }

    public List<Invoice> getAllInvoices() {
        return invoiceRepository.findAllByOrderByCodInvoiceAsc();
    }

    public List<Invoice> getInvoicesByCodInvoice(String codInvoice) {
        return invoiceRepository.findByCodInvoiceContainingIgnoreCase(codInvoice);
    }

    public Invoice getInvoiceById(long id) {
        return invoiceRepository.findInvoiceById(id);
    }

    public long getTotalInvoicesForCurrentMonth() {
        LocalDate today = LocalDate.now();

        // Primeiro dia do mês atual
        LocalDate startOfMonth = today.withDayOfMonth(1);

        // Último dia do mês atual
        LocalDate endOfMonth = today.withDayOfMonth(today.lengthOfMonth());

        // Chama o método do repositório com o intervalo de datas
        return invoiceRepository.countByCurrentMonth(startOfMonth, endOfMonth);
    }
}

