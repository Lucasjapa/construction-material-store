package projectpoo.construction_material_store.domain;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Optional;

@Entity
@Table(name = "POO_Product")
public class Product {

    @Id
    @SequenceGenerator(
            name = "poo_product_id",
            sequenceName = "poo_product_id",
            allocationSize = 1
    )
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "poo_product_id")
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String codProcuct;

    @Column
    private String description;

    @Column(nullable = false)
    private double totalStock;

    @Column(nullable = false)
    private double minStock;

    @Column(nullable = false)
    private double price;

    @Column
    private LocalDateTime expirationDate;

    @Column(nullable = false)
    private String salesUnit;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Category category;

    public Product() {
    }

    public Product(String name, String codProcuct, double totalStock, String description, double minStock, double price, String salesUnit, LocalDateTime expirationDate, Category category) {
        this.name = name;
        this.codProcuct = codProcuct;
        this.totalStock = totalStock;
        this.description = (description == null) ? "Sem descrição" : description;
        this.minStock = minStock;
        this.price = price;
        this.salesUnit = salesUnit;
        this.expirationDate = expirationDate;
        this.category = category;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCodProcuct() {
        return codProcuct;
    }

    public void setCodProcuct(String codProcuct) {
        this.codProcuct = codProcuct;
    }

    public Optional<String> getDescription() {
        return Optional.ofNullable(description);
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getTotalStock() {
        return totalStock;
    }

    public void setTotalStock(double totalStock) {
        this.totalStock = totalStock;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getMinStock() {
        return minStock;
    }

    public void setMinStock(double minStock) {
        this.minStock = minStock;
    }

    public Optional<LocalDateTime> getExpirationDate() {
        return Optional.ofNullable(expirationDate);
    }

    public void setExpirationDate(LocalDateTime expirationDate) {
        this.expirationDate = expirationDate;
    }

    public String getSalesUnit() {
        return salesUnit;
    }

    public void setSalesUnit(String salesUnit) {
        this.salesUnit = salesUnit;
    }

    public enum Category {
        CIMENTO("Cimento e Argamassas"),
        TINTA("Tintas e Acessórios"),
        FERRAMENTAS("Ferramentas"),
        ELETRICO("Materiais Elétricos"),
        HIDRAULICO("Materiais Hidráulicos"),
        MADEIRAS("Madeiras e Compensados"),
        FERRAGENS("Ferragens"),
        PISOS("Pisos e Revestimentos"),
        ILUMINACAO("Iluminação"),
        DECORACAO("Decoração"),
        JARDIM("Jardinagem"),
        OUTROS("Outros");

        private final String description;

        Category(String description) {
            this.description = description;
        }

        public String getDescricao() {
            return description;
        }
    }
}
