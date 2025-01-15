package projectpoo.construction_material_store.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import projectpoo.construction_material_store.domain.Invoice;
import projectpoo.construction_material_store.repository.InvoiceRepository;

import java.util.List;

@Service
public class InvoiceService {

    @Autowired
    private InvoiceRepository invoiceRepository;
    
    public Invoice saveInvoice(Invoice Invoice) {
        return invoiceRepository.save(Invoice);
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

