package projectpoo.construction_material_store.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import projectpoo.construction_material_store.domain.Client;

import java.util.List;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {

    List<Client> findAllByOrderByNameAsc();

    Client findClientById(Long id);

    List<Client> findByCpfCnpjContainingIgnoreCase(String cpfCnpj);
}
