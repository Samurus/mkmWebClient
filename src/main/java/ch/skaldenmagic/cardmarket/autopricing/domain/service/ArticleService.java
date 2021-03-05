package ch.skaldenmagic.cardmarket.autopricing.domain.service;

import ch.skaldenmagic.cardmarket.autopricing.domain.entity.ArticleEntity;
import ch.skaldenmagic.cardmarket.autopricing.domain.mapper.ArticleMapper;
import ch.skaldenmagic.cardmarket.autopricing.domain.mapper.ProductMapper;
import ch.skaldenmagic.cardmarket.autopricing.domain.mapper.dtos.ArticleDto;
import ch.skaldenmagic.cardmarket.autopricing.domain.repository.ArticleRepository;
import ch.skaldenmagic.cardmarket.autopricing.domain.repository.PriceRepository;
import de.cardmarket4j.entity.Article;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service to handle known Articles from the own MKM-Stock
 *
 * @author Kevin Zellweger
 * @Date 03.07.20
 */
@Service
@Transactional
public class ArticleService {

  private static final Logger LOGGER = LoggerFactory.getLogger(ArticleService.class);
  private final MkmService mkmService;
  private final ArticleRepository articleRepository;
  private final PriceRepository priceRepository;
  private final ArticleMapper articleMapper;
  private final ProductService productService;
  private final ProductMapper productMapper;

  @Autowired
  public ArticleService(
      MkmService mkmService,
      ArticleRepository articleRepository,
      PriceRepository priceRepository,
      ArticleMapper articleMapper,
      ProductService productService,
      ProductMapper productMapper) {
    this.mkmService = mkmService;
    this.articleRepository = articleRepository;
    this.priceRepository = priceRepository;
    this.articleMapper = articleMapper;
    this.productService = productService;
    this.productMapper = productMapper;
  }

  public List<ArticleEntity> findAll() {
    LOGGER.info("Fetch all Articles in Database"); //Test Log to see if Logback does something
    return articleRepository.findAll();
  }

  /**
   * Get All articles Related to a Product
   *
   * @param productId desired ProductId
   * @return related Articles
   */
  public List<ArticleEntity> findAllByProduct(Long productId) {

    return articleRepository.findAllByProductId(productId);
  }

  public List<ArticleEntity> reloadStockFromMkm() throws IOException {
    return null;
  }

  public List<ArticleDto> saveAll(List<ArticleEntity> articleEntities) {
    return articleRepository.saveAll(articleEntities).stream().map(articleMapper::entityToDto)
        .collect(
            Collectors.toList());
  }

  /**
   * Persist the whole MKM-Stock in local Database. This call might take a while, because we have to
   * check if the User might added Articles directly in MKM which are not known in the Database
   * yet.
   *
   * @param stock Result of the getStockRequest
   * @return containing all Articles
   */
  public List<ArticleDto> saveOrUpdate(List<Article> stock) {
    List<ArticleEntity> toUpdate = new ArrayList<>();
    List<ArticleEntity> toSave = new ArrayList<>();
    List<ArticleEntity> fromStock = stock.stream()
        .map(articleMapper::mkmToEntity).collect(Collectors.toList());

    fromStock.forEach(article -> {
      ArticleEntity articleEntity = articleRepository.findByArticleId(article.getArticleId());
      if (articleEntity == null) {
        // article locally not known
        toSave.add(article);
      } else {
        // article locally known but might have changed some Properties.
        toUpdate.add(articleEntity.updateSelf(article));
      }
    });
    List<ArticleDto> result = articleRepository.saveAll(toSave).stream()
        .map(articleMapper::entityToDto).collect(
            Collectors.toList());
    result.addAll(
        articleRepository.saveAll(toUpdate).stream().map(articleMapper::entityToDto).collect(
            Collectors.toList()));
    return result;
  }

  //TODO Refactor after fixing the DTO mess..... Those transactions should be easier
  public List<ArticleDto> updateArticles(List<ArticleEntity> articleEntities) {
    List<ArticleEntity> toPersist = new ArrayList<>();
    articleEntities.forEach(article -> {
      ArticleEntity articleEntity = articleRepository.findByArticleId(article.getArticleId());
      toPersist.add(articleEntity.updateSelf(article));
    });
    return articleRepository.saveAll(toPersist).stream().map(articleMapper::entityToDto).collect(
        Collectors.toList());
  }

}
