package projectpoo.construction_material_store.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import projectpoo.construction_material_store.domain.Invoice;
import projectpoo.construction_material_store.domain.Purchase;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PurchaseRepository extends JpaRepository<Purchase, Long> {

    Purchase findPurchaseById(Long id);

    List<Purchase> findPurchaseByPurchaseDate(LocalDate purchaseDate);

    List<Purchase> findAllByOrderByPurchaseDateDesc();
}
