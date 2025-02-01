package projectpoo.construction_material_store.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import projectpoo.construction_material_store.repository.InvoiceItemRepository;

import java.util.List;

@Service
public class InvoiceItemService {

    @Autowired
    private InvoiceItemRepository invoiceItemRepository;

    // Método para obter os 3 produtos mais vendidos (frequentes)
    public List<Object[]> getTop3MostFrequentProducts() {
        Pageable pageable = PageRequest.of(0, 3);
        return invoiceItemRepository.findTop3MostFrequentProducts(pageable);
    }
}

