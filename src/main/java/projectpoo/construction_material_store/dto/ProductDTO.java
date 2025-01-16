package projectpoo.construction_material_store.dto;

import projectpoo.construction_material_store.domain.Product;

import java.time.LocalDateTime;

public class ProductDTO {

    private String name;
    private String codProduct;
    private double totalStock;
    private String description;
    private double minStock;
    private double price;
    private String salesUnit;
    private LocalDateTime expirationDate;
    private String category;  // Usando String ao invés de enum para facilitar a conversão em APIs

    // Construtores
    public ProductDTO() {}

    public ProductDTO(String name, String codProduct, double totalStock, String description, double minStock, double price, String salesUnit, LocalDateTime expirationDate, String category) {
        this.name = name;
        this.codProduct = codProduct;
        this.totalStock = totalStock;
        this.description = description;
        this.minStock = minStock;
        this.price = price;
        this.salesUnit = salesUnit;
        this.expirationDate = expirationDate;
        this.category = category;
    }

    // Getters e Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCodProduct() {
        return codProduct;
    }

    public void setCodProduct(String codProduct) {
        this.codProduct = codProduct;
    }

    public double getTotalStock() {
        return totalStock;
    }

    public void setTotalStock(double totalStock) {
        this.totalStock = totalStock;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getMinStock() {
        return minStock;
    }

    public void setMinStock(double minStock) {
        this.minStock = minStock;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getSalesUnit() {
        return salesUnit;
    }

    public void setSalesUnit(String salesUnit) {
        this.salesUnit = salesUnit;
    }

    public LocalDateTime getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(LocalDateTime expirationDate) {
        this.expirationDate = expirationDate;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

//    // Método para converter de Product para ProductDTO
//    public static ProductDTO fromProduct(Product product) {
//        return new ProductDTO(
//                product.getName(),
//                product.getCodProcuct(),
//                product.getTotalStock(),
//                product.getDescription(),
//                product.getMinStock(),
//                product.getPrice(),
//                product.getSalesUnit(),
//                product.getExpirationDate(),
//                product.getCategory() != null ? product.getCategory().getDescricao() : null
//        );
//    }

    // Método para converter de ProductDTO para Product
    public Product toProduct() {
        Product.Category categoryEnum = Product.Category.fromDescription(category);
        return new Product(
                this.name,
                this.codProduct,
                this.totalStock,
                this.description,
                this.minStock,
                this.price,
                this.salesUnit,
                this.expirationDate,
                categoryEnum
        );
    }
}
