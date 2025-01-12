package projectpoo.construction_material_store.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import projectpoo.construction_material_store.domain.Product;
import projectpoo.construction_material_store.service.ProductService;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public Product createProduct(@RequestBody Product product) {
        return productService.saveProduct(product);
    }

    @GetMapping
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    @GetMapping("/{id}")
    public Product getProductById(@PathVariable Long id) {
        return productService.getProductById(id).orElse(null);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long id) {
        boolean isDeleted = productService.deleteProductById(id);
        if (isDeleted) {
            return ResponseEntity.ok("Produto excluído com sucesso!");
        } else {
            return ResponseEntity.status(404).body("Produto não encontrado.");
        }
    }

    @DeleteMapping
    public ResponseEntity<String> deleteProducts(@RequestBody List<Long> ids) {
        boolean allDeleted = productService.deleteProductsByIds(ids);
        if (allDeleted) {
            return ResponseEntity.ok("Todos os produtos foram excluídos com sucesso!");
        } else {
            return ResponseEntity.status(207).body("Alguns produtos não foram encontrados ou não puderam ser excluídos.");
        }
    }
}
