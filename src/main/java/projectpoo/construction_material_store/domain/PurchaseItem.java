package projectpoo.construction_material_store.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "POO_Purchase_Item")
public class PurchaseItem {

    @Id
    @SequenceGenerator(
            name = "poo_purchase_item_id",
            sequenceName = "poo_purchase_item_id",
            allocationSize = 1
    )
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "poo_purchase_item_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_produto", nullable = false)
    private Product product;

    @Column(nullable = false)
    private long quantityPurchase;

    @Column(nullable = false)
    private double unitPrice;

    public PurchaseItem() {
    }

    public PurchaseItem(Product product, long quantityPurchase, double unitPrice) {
        this.product = product;
        this.quantityPurchase = quantityPurchase;
        this.unitPrice = unitPrice;
    }

    public Long getId() {
        return id;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
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
