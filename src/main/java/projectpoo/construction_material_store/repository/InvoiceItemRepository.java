package projectpoo.construction_material_store.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import projectpoo.construction_material_store.domain.InvoiceItem;

import java.util.List;

@Repository
public interface InvoiceItemRepository extends JpaRepository<InvoiceItem, Long> {

    @Query("SELECT p.name, COUNT(ii.id) AS frequency " +
            "FROM Product p " +
            "JOIN InvoiceItem ii ON ii.product.id = p.id " +
            "GROUP BY p.name " +
            "ORDER BY frequency DESC")
    List<Object[]> findTop3MostFrequentProducts(Pageable pageable);

    @Query("SELECT p.name, COUNT(ii.id) AS frequency " +
            "FROM Product p " +
            "JOIN InvoiceItem ii ON ii.product.id = p.id " +
            "GROUP BY p.name " +
            "ORDER BY frequency ASC")
    List<Object[]> findTop3LeastFrequentProducts(Pageable pageable);
}
