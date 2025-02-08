package projectpoo.construction_material_store.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import projectpoo.construction_material_store.domain.*;
import projectpoo.construction_material_store.dto.InvoiceDTO;
import projectpoo.construction_material_store.dto.InvoiceItemDTO;
import projectpoo.construction_material_store.dto.PurchaseDTO;
import projectpoo.construction_material_store.dto.PurchaseItemDTO;
import projectpoo.construction_material_store.repository.InvoiceRepository;
import projectpoo.construction_material_store.repository.PurchaseRepository;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class PurchaseService {

    @Autowired
    private PurchaseRepository purchaseRepository;

    @Autowired
    private ProductService productService;

    public Purchase savePurchase(PurchaseDTO purchaseDTO) {
        List<PurchaseItem> purchaseItems = new ArrayList<>();

        for (PurchaseItemDTO purchaseItemDTO : purchaseDTO.getPurchaseItens()) {
            Product product = productService.getProductById(purchaseItemDTO.getProduct().getId());
            product.setTotalStock(product.getTotalStock() + purchaseItemDTO.getQuantityPurchase());
            purchaseItems.add(new PurchaseItem(product, purchaseItemDTO.getQuantityPurchase(), purchaseItemDTO.getUnitPrice()));
        }

        return purchaseRepository.save(purchaseDTO.toInvoice(purchaseItems));
    }

    public List<Purchase> getAllPurchases() {
        return purchaseRepository.findAllByOrderByPurchaseDateDesc();
    }

    public List<Purchase> getPurchaseByDate(String purchaseDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        LocalDate date = LocalDate.parse(purchaseDate, formatter);
        return purchaseRepository.findPurchaseByPurchaseDate(date);
    }

    public Purchase getPurchaseById(long id) {
        return purchaseRepository.findPurchaseById(id);
    }

}

