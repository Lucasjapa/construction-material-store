package projectpoo.construction_material_store.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import projectpoo.construction_material_store.domain.Client;
import projectpoo.construction_material_store.domain.Invoice;

import java.util.List;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {

    List<Invoice> findAllByOrderByCodInvoiceAsc();

    Invoice findInvoiceById(Long id);

    List<Invoice> findByCodInvoiceContainingIgnoreCase(String codInvoice);
}
