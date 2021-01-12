package ch.skaldenmagic.cardmarket.autopricing.domain.repository;

import ch.skaldenmagic.cardmarket.autopricing.domain.entity.LocalizationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Kevin Zellweger
 * @Date 12.01.21
 */
public interface LocalizationRepository extends JpaRepository<LocalizationEntity, Long> {

}
