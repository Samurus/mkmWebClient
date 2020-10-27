package ch.softridge.cardmarket.autopricing.repository;

import ch.softridge.cardmarket.autopricing.repository.model.ArticleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticleRepository extends JpaRepository<ArticleEntity, Long> {

    @Query( "select o from ArticleEntity o where productId in :productIdList" )
    List<ArticleEntity> findByProductIds(@Param("productIdList") List<Integer> productIdList);

}
