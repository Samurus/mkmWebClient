package ch.softridge.cardmarket.autopricing.repository;

import ch.softridge.cardmarket.autopricing.repository.model.ArticleEntity;
import ch.softridge.cardmarket.autopricing.repository.model.ArticlePrice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Kevin Zellweger
 * @Date 03.07.20
 */
@Repository
public interface PriceRepository extends JpaRepository<ArticlePrice, Long> {
}
