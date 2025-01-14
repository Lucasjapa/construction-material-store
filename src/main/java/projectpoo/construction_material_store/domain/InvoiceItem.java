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

}
