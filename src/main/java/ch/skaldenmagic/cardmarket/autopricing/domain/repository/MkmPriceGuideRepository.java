package ch.skaldenmagic.cardmarket.autopricing.domain.repository;

import ch.skaldenmagic.cardmarket.autopricing.domain.entity.MkmPriceGuide;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Kevin Zellweger
 * @Date 11.03.21
 */
public interface MkmPriceGuideRepository extends JpaRepository<MkmPriceGuide, Long> {

}
