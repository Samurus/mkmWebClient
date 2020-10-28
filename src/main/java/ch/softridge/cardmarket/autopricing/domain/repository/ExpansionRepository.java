package ch.softridge.cardmarket.autopricing.domain.repository;

import ch.softridge.cardmarket.autopricing.domain.entity.ExpansionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExpansionRepository extends JpaRepository<ExpansionEntity, Long> {
    List<ExpansionEntity> findAllByNameContaining(String name);
}
