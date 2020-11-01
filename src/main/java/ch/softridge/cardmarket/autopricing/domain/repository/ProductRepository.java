package ch.softridge.cardmarket.autopricing.domain.repository;

import ch.softridge.cardmarket.autopricing.domain.entity.ProductEntity;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Long> {

  List<ProductEntity> findAllByExpansionId(Integer expansionId);

  @Query(value = "select max (p.dateAdded) from ProductEntity p")
  LocalDate getMostRecentDateAdded();

  Optional<ProductEntity> findByProductId(Integer productId);
}
