package ch.softridge.cardmarket.autopricing.repository;

import ch.softridge.cardmarket.autopricing.service.model.ScryfallCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Kevin Zellweger
 * @Date 04.07.20
 */
@Repository
public interface ScryfallCardRepository extends JpaRepository<ScryfallCard,Long> {
}
