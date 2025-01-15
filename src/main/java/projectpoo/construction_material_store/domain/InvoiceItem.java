package projectpoo.construction_material_store.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "POO_Invoice_Item")
public class InvoiceItem {

    @Id
    @SequenceGenerator(
            name = "poo_invoice_item_id",
            sequenceName = "poo_invoice_item_id",
            allocationSize = 1
    )
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "poo_invoice_item_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_produto", nullable = false)
    private Product product;

    @ManyToOne
    @JoinColumn(name = "id_invoice", nullable = false)
    private Invoice invoice;

    @Column(nullable = false)
    private Integer quantitySale;

    @Column(nullable = false)
    private Double unitPrice;

    public InvoiceItem() {
    }

    public InvoiceItem(Product product, Invoice invoice, Integer quantitySale, Double unitPrice) {
        this.product = product;
        this.invoice = invoice;
        this.quantitySale = quantitySale;
        this.unitPrice = unitPrice;
    }

    public Invoice getInvoice() {
        return invoice;
    }

    public void setInvoice(Invoice invoice) {
        this.invoice = invoice;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
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
