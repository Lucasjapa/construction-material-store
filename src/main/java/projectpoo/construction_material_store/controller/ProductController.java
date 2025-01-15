package projectpoo.construction_material_store.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import projectpoo.construction_material_store.domain.Product;
import projectpoo.construction_material_store.service.ProductService;

import java.util.List;

@RestController
@RequestMapping("/products")
// Define a classe como um controlador REST e mapeia as requisições para o endpoint "/products"
public class ProductController {

    private final ProductService productService;

    // Construtor que injeta a dependência do serviço de produtos (ProductService)
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    // Mapeia uma requisição POST para criar um novo produto
    public Product createProduct(@RequestBody Product product) {
        // Chama o serviço para salvar o produto recebido no corpo da requisição
        return productService.saveProduct(product);
    }

    @GetMapping
    // Mapeia uma requisição GET para buscar todos os produtos
    public List<Product> getAllProducts() {
        // Retorna a lista de todos os produtos disponíveis
        return productService.getAllProducts();
    }

    @GetMapping("/{id}")
    // Mapeia uma requisição GET para buscar um produto pelo seu ID
    public Product getProductById(@PathVariable Long id) {
        // Retorna o produto correspondente ao ID fornecido, ou null caso não encontrado
        return productService.getProductById(id).orElse(null);
    }

    @DeleteMapping("/{id}")
    // Mapeia uma requisição DELETE para excluir um produto pelo ID
    public ResponseEntity<String> deleteProduct(@PathVariable Long id) {
        // Verifica se o produto foi excluído com sucesso
        boolean isDeleted = productService.deleteProductById(id);
        if (isDeleted) {
            return ResponseEntity.ok("Produto excluído com sucesso!");
        } else {
            // Retorna uma mensagem de erro caso o produto não seja encontrado
            return ResponseEntity.status(404).body("Produto não encontrado.");
        }
    }

    @DeleteMapping
    // Mapeia uma requisição DELETE para excluir múltiplos produtos com base em uma lista de IDs
    public ResponseEntity<String> deleteProducts(@RequestBody List<Long> ids) {
        // Verifica se todos os produtos da lista foram excluídos com sucesso
        boolean allDeleted = productService.deleteProductsByIds(ids);
        if (allDeleted) {
            return ResponseEntity.ok("Todos os produtos foram excluídos com sucesso!");
        } else {
            // Retorna um status 207 (Multi-Status) caso alguns produtos não tenham sido encontrados ou excluídos
            return ResponseEntity.status(207).body("Alguns produtos não foram encontrados ou não puderam ser excluídos.");
        }
    }
}
