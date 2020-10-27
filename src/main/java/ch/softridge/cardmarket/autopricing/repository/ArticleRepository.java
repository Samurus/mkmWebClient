package ch.softridge.cardmarket.autopricing.repository;

import ch.softridge.cardmarket.autopricing.repository.model.ArticleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticleRepository extends JpaRepository<ArticleEntity, Long> {
}
