package projectpoo.construction_material_store.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import projectpoo.construction_material_store.domain.Product;
import projectpoo.construction_material_store.repository.ProductRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    // Método para salvar um novo produto
    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }

    // Método para buscar todos os produtos
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    // Método para buscar um produto pelo nome
    public List<Product> getProductsByName(String name) {
        return productRepository.findByName(name);
    }

    // Método para buscar um produto pelo id
    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    // Método para deletar um produto
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    public boolean deleteProductById(Long id) {
        if (productRepository.existsById(id)) {
            productRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }

    public boolean deleteProductsByIds(List<Long> ids) {
        List<Product> productsToDelete = productRepository.findAllById(ids);
        if (!productsToDelete.isEmpty()) {
            productRepository.deleteAll(productsToDelete);
            return true;
        }
        return false;
    }
}

