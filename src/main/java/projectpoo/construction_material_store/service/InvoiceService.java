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

    public List<Invoice> getAllInvoices() {
        return invoiceRepository.findAll();
    }

    public List<Invoice> getInvoicesByCpfOrCpnj(String cpfCnpj) {
        return invoiceRepository.findByClient_CpfCnpj(cpfCnpj);
    }

    public void deleteInvoice(Long id) {
        invoiceRepository.deleteById(id);
    }

    public boolean deleteInvoiceById(Long id) {
        if (invoiceRepository.existsById(id)) {
            invoiceRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }

    public boolean deleteInvoicesByIds(List<Long> ids) {
        List<Invoice> InvoicesToDelete = invoiceRepository.findAllById(ids);
        if (!InvoicesToDelete.isEmpty()) {
            invoiceRepository.deleteAll(InvoicesToDelete);
            return true;
        }
        return false;
    }
}

