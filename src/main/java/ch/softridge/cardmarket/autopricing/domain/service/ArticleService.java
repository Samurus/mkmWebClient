package ch.softridge.cardmarket.autopricing.domain.service;

import static java.util.stream.Collectors.groupingBy;

import ch.softridge.cardmarket.autopricing.domain.entity.ArticleEntity;
import ch.softridge.cardmarket.autopricing.domain.entity.ArticlePriceEntity;
import ch.softridge.cardmarket.autopricing.domain.entity.ProductEntity;
import ch.softridge.cardmarket.autopricing.domain.mapper.ArticleMapper;
import ch.softridge.cardmarket.autopricing.domain.mapper.ProductMapper;
import ch.softridge.cardmarket.autopricing.domain.mapper.dtos.ArticleDto;
import ch.softridge.cardmarket.autopricing.domain.repository.ArticleRepository;
import ch.softridge.cardmarket.autopricing.domain.repository.PriceRepository;
import de.cardmarket4j.entity.Article;
import de.cardmarket4j.entity.CardMarketArticle;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Kevin Zellweger
 * @Date 03.07.20
 */
@Service
@Transactional
public class ArticleService {

  private static final Logger log = LoggerFactory.getLogger(ArticleService.class);

  @Autowired
  private MkmService mkmService;

  @Autowired
  private ArticleRepository articleRepository;

  @Autowired
  private PriceRepository priceRepository;

  @Autowired
  private ArticleMapper articleMapper;

  @Autowired
  private ProductService productService;

  @Autowired
  private ProductMapper productMapper;


  public List<ArticleEntity> reloadStockFromMkm() throws IOException {
    articleRepository.deleteAll();
    List<Article> stock = mkmService.getCardMarket().getStockService().getStock();
    log.info("Requests used today: " + mkmService.getCardMarket().getRequestCount());
    List<ArticleEntity> stockEntities = stock.stream().map(articleMapper::apiArticleToEntity)
        .collect(Collectors.toList());

    Map<Integer, ProductEntity> existingProducts = productService
        .findByProductIdInList(stockEntities.stream()
            .map(product -> product.getProduct().getProductId())
            .collect(Collectors.toList())).stream()
        .collect(Collectors.toMap(ProductEntity::getProductId, Function.identity()));

    stockEntities.forEach(articleEntity -> {
      ProductEntity existingProduct = existingProducts
          .get(articleEntity.getProduct().getProductId());

      if (null == existingProduct) {
        //TODO improvement: first determine all new ones and create in bulk
        existingProduct = productService.saveNewProduct(articleEntity.getProduct());
      } else {
        productMapper.updateSecondWithFirst(articleEntity.getProduct(), existingProduct);
        existingProduct = productService.saveNewProduct(existingProduct);
      }
      existingProducts.put(existingProduct.getProductId(), existingProduct);
      articleEntity.setProduct(existingProduct);
    });
    return articleRepository.saveAll(stockEntities);
  }


  public List<ArticleEntity> saveAll(List<ArticleEntity> articleEntities) {
    return articleRepository.saveAll(articleEntities);
  }


  public List<ArticleEntity> updateAll(List<ArticleDto> articleDtos) throws IOException {
    List<CardMarketArticle> entities = articleDtos.stream().map(articleMapper::dtoToArticle)
        .collect(Collectors.toList());
    List<Article> articles = mkmService.getCardMarket().getStockService()
        .editListArticles(entities);
    List<ArticleEntity> articleEntities = articles.stream().map(articleMapper::apiArticleToEntity)
        .collect(Collectors.toList());

    List<Integer> collect = articleEntities.stream().map(ArticleEntity::getArticleId)
        .collect(Collectors.toList());
    List<ArticleEntity> byArticleIds = articleRepository.findByArticleIds(collect);
    articleRepository.deleteInBatch(byArticleIds);
    return articleRepository.saveAll(articleEntities);
  }

  public List<ArticleDto> findAllWithMinPrice() {
    //TODO optimization with Databasequeries or Entities with oneToMany
    List<ArticleEntity> articles = articleRepository.findAll();
    List<ArticleDto> articleDtos = articles.stream().map(articleMapper::articleEntityToDto)
        .collect(Collectors.toList());
    List<ArticlePriceEntity> byArticleId = priceRepository.findAll();
    Map<Integer, List<ArticlePriceEntity>> prices = byArticleId.stream()
        .collect(groupingBy(ArticlePriceEntity::getArticleId));

    List<ArticleDto> allWithPrices = new ArrayList<>();
    articleDtos.forEach(articleEntity -> {
      ArticlePriceEntity cheapestPrice = prices.get(articleEntity.getArticleId()).stream()
          .min(Comparator.comparing(ArticlePriceEntity::getPrice))
          .orElseThrow(NoSuchElementException::new);
      articleEntity.setArticlePriceEntity(cheapestPrice);
      articleEntity.setPrice(cheapestPrice.getRecommendedPrice());
      allWithPrices.add(articleEntity);
    });

    return allWithPrices;
  }

  public List<ArticleDto> findAllArticlesWithCheapestPriceByExpansion(Integer expansionId)
      throws IOException {
    List<ProductEntity> productsByExpansionId = productService.findAllByExpansionId(expansionId);
    List<Integer> productIds = productsByExpansionId.stream().map(ProductEntity::getProductId)
        .collect(Collectors.toList());
    List<ArticleEntity> byProductIds = articleRepository.findByProductIds(productIds);

    List<ArticleDto> articleDtos = byProductIds.stream().map(articleMapper::articleEntityToDto)
        .collect(Collectors.toList());

    List<ArticleDto> allArticlesWithCheapestPriceByExpansion = new ArrayList<>();
    articleDtos.forEach(articleEntity -> {
      List<ArticlePriceEntity> byArticleId = priceRepository
          .findByArticleId(articleEntity.getArticleId());
      ArticlePriceEntity cheapestPrice = byArticleId.stream()
          .min(Comparator.comparing(ArticlePriceEntity::getPrice))
          .orElseThrow(NoSuchElementException::new);
      articleEntity.setArticlePriceEntity(cheapestPrice);
      articleEntity.setPrice(cheapestPrice.getRecommendedPrice());
      allArticlesWithCheapestPriceByExpansion.add(articleEntity);
    });

    return allArticlesWithCheapestPriceByExpansion;
  }

  public List<ArticleEntity> findAll() {
    return articleRepository.findAll();
  }

}
