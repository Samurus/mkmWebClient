package ch.softridge.cardmarket.autopricing.domain.repository;

import ch.softridge.cardmarket.autopricing.domain.entity.ArticleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticleRepository extends JpaRepository<ArticleEntity, Long> {

    @Query( "select o from ArticleEntity o where productId in :productIdList" )
    List<ArticleEntity> findByProductIds(@Param("productIdList") List<Integer> productIdList);

    @Query( "select o from ArticleEntity o where articleId in :articleIds" )
    List<ArticleEntity> findByArticleIds(@Param("articleIds") List<Integer> articleIds);

}
