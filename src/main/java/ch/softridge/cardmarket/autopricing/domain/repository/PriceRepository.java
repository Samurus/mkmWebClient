package ch.softridge.cardmarket.autopricing.domain.repository;

import ch.softridge.cardmarket.autopricing.domain.entity.ArticlePriceEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * @author Kevin Zellweger
 * @Date 03.07.20
 */
@Repository
public interface PriceRepository extends JpaRepository<ArticlePriceEntity, Long> {

  @Query("select o from ArticlePriceEntity o where articleId in :articleIdList")
  List<ArticlePriceEntity> findByArticleIds(@Param("articleIdList") List<Integer> articleIdList);

  List<ArticlePriceEntity> findByArticleId(Integer articleId);

}
