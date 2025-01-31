package projectpoo.construction_material_store.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import projectpoo.construction_material_store.domain.Client;
import projectpoo.construction_material_store.domain.Invoice;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {

    List<Invoice> findAllByOrderByCodInvoiceAsc();

    Invoice findInvoiceById(Long id);

    List<Invoice> findByCodInvoiceContainingIgnoreCase(String codInvoice);

    @Query("SELECT COUNT(i) FROM Invoice i WHERE i.saleDate BETWEEN :startOfMonth AND :endOfMonth")
    long countByCurrentMonth(@Param("startOfMonth") LocalDate startOfMonth, @Param("endOfMonth") LocalDate endOfMonth);
}
