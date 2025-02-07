package projectpoo.construction_material_store.domain;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "POO_Purchase")
public class Purchase {

    @Id
    @SequenceGenerator(
            name = "poo_purchase_id",
            sequenceName = "poo_purchase_id",
            allocationSize = 1
    )
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "poo_purchase_id")
    private Long id;

    @Column(nullable = false)
    private double totalPrice;

    @Column(nullable = false)
    private LocalDate purchaseDate = LocalDate.now();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "id_purchase")
    private List<PurchaseItem> purchaseItems;


    public Purchase() {
    }

    public Purchase(double totalPrice, List<PurchaseItem> purchaseItems) {
        this.totalPrice = totalPrice;
        this.purchaseItems = purchaseItems;
    }

    public Long getId() {
        return id;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public LocalDate getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(LocalDate purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public List<PurchaseItem> getPurchaseItems() {
        return purchaseItems;
    }

    public void setPurchaseItems(List<PurchaseItem> purchaseItems) {
        this.purchaseItems = purchaseItems;
    }
}
