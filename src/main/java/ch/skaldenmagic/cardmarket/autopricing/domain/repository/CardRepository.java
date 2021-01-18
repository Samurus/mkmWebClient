package ch.skaldenmagic.cardmarket.autopricing.domain.repository;

import ch.skaldenmagic.cardmarket.autopricing.domain.model.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Kevin Zellweger
 * @Date 03.07.20
 */
@Repository
public interface CardRepository extends JpaRepository<Card, Long> {

}
