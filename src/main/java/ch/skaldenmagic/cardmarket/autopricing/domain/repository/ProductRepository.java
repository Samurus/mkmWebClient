package ch.skaldenmagic.cardmarket.autopricing.domain.repository;

import ch.skaldenmagic.cardmarket.autopricing.domain.entity.ProductEntity;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Long> {

  List<ProductEntity> findAllByExpansionId(Integer expansionId);

  List<ProductEntity> findAllByExpansionNameContaining(String name);

  @Query(value = "select max (p.dateAdded) from ProductEntity p")
  LocalDate getMostRecentDateAdded();

  @Query(value = "select p from ProductEntity p where product_id in :productIdList")
  List<ProductEntity> findByProductIdInList(List<Integer> productIdList);

  @Query(value = "SELECT distinct p.expansionName FROM ProductEntity p WHERE category_id = :i")
  List<String> findExpansionNamesDistinctByCategoryId(int i);

}
