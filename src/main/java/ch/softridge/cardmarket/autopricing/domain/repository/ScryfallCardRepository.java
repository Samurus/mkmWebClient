package ch.softridge.cardmarket.autopricing.domain.repository;

import ch.softridge.cardmarket.autopricing.domain.model.ScryfallCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Kevin Zellweger
 * @Date 04.07.20
 */
@Repository
public interface ScryfallCardRepository extends JpaRepository<ScryfallCard, Long> {

}
