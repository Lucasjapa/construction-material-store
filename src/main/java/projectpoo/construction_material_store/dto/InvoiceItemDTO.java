package projectpoo.construction_material_store.dto;

import projectpoo.construction_material_store.domain.InvoiceItem;

public class InvoiceItemDTO {

    private long id;
    private ProductDTO product;
    private Integer quantitySale;
    private Double unitPrice;

    public InvoiceItemDTO() {
    }

    public InvoiceItemDTO(ProductDTO product, Integer quantitySale, Double unitPrice) {
        this.product = product;
        this.quantitySale = quantitySale;
        this.unitPrice = unitPrice;
    }

    public InvoiceItemDTO(InvoiceItem invoiceItem) {
        this.id = invoiceItem.getId();
        this.product = invoiceItem.getProduct().toDTO();
        this.quantitySale = invoiceItem.getQuantitySale();
        this.unitPrice = invoiceItem.getUnitPrice();
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

    public Integer getQuantitySale() {
        return quantitySale;
    }

    public void setQuantitySale(Integer quantitySale) {
        this.quantitySale = quantitySale;
    }

    public Double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(Double unitPrice) {
        this.unitPrice = unitPrice;
    }
}
