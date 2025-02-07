package projectpoo.construction_material_store.dto;

import projectpoo.construction_material_store.domain.InvoiceItem;
import projectpoo.construction_material_store.domain.PurchaseItem;

public class PurchaseItemDTO {

    private long id;
    private ProductDTO product;
    private long quantityPurchase;
    private double unitPrice;

    public PurchaseItemDTO() {
    }

    public PurchaseItemDTO(ProductDTO product, int quantitySale, double unitPrice) {
        this.product = product;
        this.quantityPurchase = quantitySale;
        this.unitPrice = unitPrice;
    }

    public PurchaseItemDTO(PurchaseItem purchaseItem) {
        this.id = purchaseItem.getId();
        this.product = purchaseItem.getProduct().toDTO();
        this.quantityPurchase = purchaseItem.getQuantityPurchase();
        this.unitPrice = purchaseItem.getUnitPrice();
    }

    public long getId() {
        return id;
    }

    public ProductDTO getProduct() {
        return product;
    }

    public void setProduct(ProductDTO product) {
        this.product = product;
    }

    public long getQuantityPurchase() {
        return quantityPurchase;
    }

    public void setQuantityPurchase(long quantityPurchase) {
        this.quantityPurchase = quantityPurchase;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }
}
