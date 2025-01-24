package projectpoo.construction_material_store.dto;

public class InvoiceItemDTO {

    private long id;
    private ProductDTO product;
    private String quantitySale;
    private String unitPrice;

    public InvoiceItemDTO() {
    }

    public InvoiceItemDTO(ProductDTO product, String quantitySale, String unitPrice) {
        this.product = product;
        this.quantitySale = quantitySale;
        this.unitPrice = unitPrice;
    }

    public InvoiceItemDTO(InvoiceItemDTO invoiceItemDTO) {
        this.id = invoiceItemDTO.getId();
        this.product = invoiceItemDTO.getProduct();
        this.quantitySale = invoiceItemDTO.getQuantitySale();
        this.unitPrice = invoiceItemDTO.getUnitPrice();
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

    public String getQuantitySale() {
        return quantitySale;
    }

    public void setQuantitySale(String quantitySale) {
        this.quantitySale = quantitySale;
    }

    public String getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(String unitPrice) {
        this.unitPrice = unitPrice;
    }
}
