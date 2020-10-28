package ch.softridge.cardmarket.autopricing.domain.repository;

import ch.softridge.cardmarket.autopricing.domain.entity.ArticlePrice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Kevin Zellweger
 * @Date 03.07.20
 */
@Repository
public interface PriceRepository extends JpaRepository<ArticlePrice, Long> {

    @Query( "select o from ArticlePrice o where articleId in :articleIdList" )
    List<ArticlePrice> findByArticleIds(@Param("articleIdList") List<Integer> articleIdList);

    List<ArticlePrice> findByArticleId(Integer articleId);

}
