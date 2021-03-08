package ch.skaldenmagic.cardmarket.autopricing.domain.service;

import ch.skaldenmagic.cardmarket.autopricing.domain.entity.ArticleEntity;
import ch.skaldenmagic.cardmarket.autopricing.domain.entity.ProductEntity;
import ch.skaldenmagic.cardmarket.autopricing.domain.mapper.ArticleMapper;
import ch.skaldenmagic.cardmarket.autopricing.domain.mapper.dtos.ArticleDto;
import ch.skaldenmagic.cardmarket.autopricing.domain.service.exceptions.MkmAPIException;
import de.cardmarket4j.entity.Article;
import de.cardmarket4j.entity.CardMarketArticle;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service to Handle all Interactions with the Stock of the User The Services depends on the Entity
 * Services
 * <ul>{@link ArticleService}</ul>
 *
 * @author Kevin Zellweger
 * @Date 17.01.21
 */
@Service
public class StockService {

  private final Logger LOG = LoggerFactory.getLogger(StockService.class);
  private final MkmService mkmService;
  private final ArticleService articleService;
  private final ArticleMapper articleMapper;
  private final ProductService productService;

  @Autowired
  public StockService(MkmService mkmService, ArticleMapper articleMapper,
      ArticleService articleService, ProductService productService) {
    this.mkmService = mkmService;
    this.articleService = articleService;
    this.articleMapper = articleMapper;
    this.productService = productService;
  }

  /**
   * Request all Articles from the MKM Stock.
   *
   * @return List of ArticleDTOs
   */
  public List<ArticleDto> getAllArticlesFromStock() {
    List<ArticleDto> result;
    List<Article> stock;
    try {
      stock = mkmService.getCardMarket().getStockService().getStock();
    } catch (IOException e) {
      throw new MkmAPIException(de.cardmarket4j.service.StockService.class, "getStock()");
    }
    result = articleService.saveOrUpdate(stock);
    return result;
  }

  /**
   * Post new Articles to MKM Stock.
   *
   * @param articleDtos articles to Post
   * @return Uploaded Articles as Dtos.
   */
  public List<ArticleDto> postNewArticlesToStock(List<ArticleDto> articleDtos) {
    try {
      List<CardMarketArticle> articlesToPost = articleDtos.stream()
          .filter(articleDto -> !articleDto.getProduct().getName().equals("Unknown Card"))
          .map(articleMapper::dtoToMkm)
          .collect(
              Collectors.toList());
      List<ArticleEntity> postedArticles = new ArrayList<>();
      int last = 0;
      int size = articlesToPost.size();

      while (size - last >= 100) {
        postedArticles.addAll(insertArticleList(articlesToPost, last, last += 100));
      }
      if (last < size) {
        postedArticles.addAll(insertArticleList(articlesToPost, last, size));
      }

      for (ArticleEntity articleEntity : postedArticles) {
        ProductEntity productEntity = productService
            .findByProductId(articleEntity.getProduct().getProductId()).orElseThrow(
                () -> new MkmAPIException(ProductService.class, "findProductByID()")
            );
        articleEntity.setProduct(productEntity);
      }
      return articleService.saveAll(postedArticles);
    } catch (NullPointerException inEx) {
      throw new MkmAPIException(StockService.class, "postNewArticlesToStock");
    }
  }

  public List<ArticleDto> updateArticlesInStock(List<ArticleDto> articleDtos) {
    List<CardMarketArticle> articlesToUpdate = articleDtos.stream()
        .map(articleMapper::dtoToMkm)
        .collect(
            Collectors.toList());
    List<ArticleEntity> updatedArticles = new ArrayList<>();
    int last = 0;
    int size = articlesToUpdate.size();

    while (size - last >= 100) {
      updatedArticles.addAll(updateArticleList(articlesToUpdate, last, last += 100));
    }
    if (last < size) {
      updatedArticles.addAll(updateArticleList(articlesToUpdate, last, size));
    }
    return updatedArticles.stream().map(articleMapper::entityToDto).collect(Collectors.toList());
  }

  //TODO: could be a Strategy if update or insert

  private List<ArticleEntity> insertArticleList(List<CardMarketArticle> articles, int start,
      int stop) {
    List<Article> result;
    try {
      result = mkmService.getCardMarket().getStockService()
          .insertListArticles(articles.subList(start, stop));
    } catch (IOException e) {
      LOG.error(e.getMessage());
      throw new MkmAPIException(de.cardmarket4j.service.StockService.class,
          "inserListArticles()");
    }
    return result.stream().map(articleMapper::mkmToEntity).collect(Collectors.toList());
  }

  private List<ArticleEntity> updateArticleList(List<CardMarketArticle> articles, int start,
      int stop) {
    List<Article> result;
    try {
      result = mkmService.getCardMarket().getStockService()
          .editListArticles(articles.subList(start, stop));
    } catch (IOException e) {
      LOG.error(e.getMessage());
      throw new MkmAPIException(de.cardmarket4j.service.StockService.class, "editListArticles()");
    }
    return result.stream().map(articleMapper::mkmToEntity).collect(Collectors.toList());
  }

}
