package projectpoo.construction_material_store.domain;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "POO_Invoice")
public class Invoice {

    @Id
    @SequenceGenerator(
            name = "poo_invoice_id",
            sequenceName = "poo_invoice_id",
            allocationSize = 1
    )
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "poo_invoice_id")
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_cliente", nullable = false)
    private Client client;

    @Column(nullable = false)
    private Double totalPrice;

    @Column(nullable = false)
    private String codInvoice;

    @Column(nullable = false)
    private LocalDate saleDate = LocalDate.now();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "id_invoice")
    private List<InvoiceItem> invoiceItems;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    public Invoice() {
    }

    public Invoice(Client client, Double totalPrice, List<InvoiceItem> invoiceItems, Status status) {
        this.client = client;
        this.totalPrice = totalPrice;
        this.invoiceItems = invoiceItems;
        this.status = status;
        this.codInvoice = UUID.randomUUID().toString().replace("-", "").substring(0, 15);
    }

    public Long getId() {
        return id;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public LocalDate getSaleDate() {
        return saleDate;
    }

    public void setSaleDate(LocalDate saleDate) {
        this.saleDate = saleDate;
    }

    public List<InvoiceItem> getInvoiceItems() {
        return invoiceItems;
    }

    public void setInvoiceItems(List<InvoiceItem> invoiceItems) {
        this.invoiceItems = invoiceItems;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getCodInvoice() {
        return codInvoice;
    }

    public enum Status {
        FINALIZADO,
        CANCELADO,
        ESTORNADO
    }
}
