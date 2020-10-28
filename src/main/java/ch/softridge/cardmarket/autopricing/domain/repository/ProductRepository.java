package ch.softridge.cardmarket.autopricing.domain.repository;

import ch.softridge.cardmarket.autopricing.domain.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Long> {
    List<ProductEntity> findAllByExpansionId(Integer expansionId);
}
