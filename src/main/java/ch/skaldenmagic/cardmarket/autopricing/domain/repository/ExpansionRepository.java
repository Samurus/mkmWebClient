package ch.skaldenmagic.cardmarket.autopricing.domain.repository;

import ch.skaldenmagic.cardmarket.autopricing.domain.entity.ExpansionEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExpansionRepository extends JpaRepository<ExpansionEntity, Long> {

  List<ExpansionEntity> findAllByNameContaining(String name);

  ExpansionEntity getByExpansionId(Integer expansionId);
}
