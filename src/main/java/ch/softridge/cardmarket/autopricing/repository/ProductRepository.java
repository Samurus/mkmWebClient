package ch.softridge.cardmarket.autopricing.repository;

import ch.softridge.cardmarket.autopricing.repository.model.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Long> {
}
