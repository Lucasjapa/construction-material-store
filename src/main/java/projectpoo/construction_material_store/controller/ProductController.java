package projectpoo.construction_material_store.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import projectpoo.construction_material_store.domain.Product;
import projectpoo.construction_material_store.dto.ProductDTO;
import projectpoo.construction_material_store.service.ProductService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/products")
// Define a classe como um controlador REST e mapeia as requisições para o endpoint "/products"
public class ProductController {

    @Autowired
    private ProductService productService;

    @PostMapping
    // Mapeia uma requisição POST para criar um novo produto
    public Product createProduct(@RequestBody ProductDTO product) {
        // Chama o serviço para salvar o produto recebido no corpo da requisição
        return productService.saveProduct(product);
    }

    @GetMapping
    // Mapeia uma requisição GET para buscar todos os produtos
    public List<ProductDTO> getAllProducts() {
        return productService.getAllProducts().stream()
                .map(ProductDTO::new)  // Converte cada produto para ProductDTO
                .collect(Collectors.toList());
    }

    @GetMapping("/search-products/{name}")
    // Mapeia uma requisição GET para buscar todos os produtos pelo nome informado
    public List<ProductDTO> getProductsByName(@PathVariable String name) {
        return productService.getProductsByName(name).stream()
                .map(ProductDTO::new)  // Converte cada produto para ProductDTO
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    // Mapeia uma requisição GET para buscar um produto pelo seu ID
    public ResponseEntity<ProductDTO> getProductById(@PathVariable Long id) {
        Product product = productService.getProductById(id);

        if (product != null) {
            return ResponseEntity.ok(new ProductDTO(product));  // Produto encontrado, retorna 200 OK
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);  // Produto não encontrado, retorna 404 Not Found
        }
    }

    @PutMapping("/updateproduct/{id}")
    // Mapeia uma requisição PUT para atualizar um produto pelo ID
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @RequestBody ProductDTO productDTO) {
        // Verifica se o produto existe
        Product existingProduct = productService.getProductById(id);

        if (existingProduct != null) {
            Product savedProduct = productService.updateProduct(productDTO, existingProduct);
            return ResponseEntity.ok(savedProduct);
        } else {
            // Retorna um status 404 caso o produto não seja encontrado
            return ResponseEntity.status(404).body(null);
        }
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
