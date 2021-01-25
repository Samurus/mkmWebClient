package ch.skaldenmagic.cardmarket.autopricing.domain.service;

import static java.util.stream.Collectors.groupingBy;

import ch.skaldenmagic.cardmarket.autopricing.domain.entity.ArticleEntity;
import ch.skaldenmagic.cardmarket.autopricing.domain.entity.ArticlePriceEntity;
import ch.skaldenmagic.cardmarket.autopricing.domain.entity.ProductEntity;
import ch.skaldenmagic.cardmarket.autopricing.domain.mapper.ArticleMapper;
import ch.skaldenmagic.cardmarket.autopricing.domain.mapper.ArticlePriceMapper;
import ch.skaldenmagic.cardmarket.autopricing.domain.mapper.ProductMapper;
import ch.skaldenmagic.cardmarket.autopricing.domain.mapper.dtos.ArticleDto;
import ch.skaldenmagic.cardmarket.autopricing.domain.repository.ArticleRepository;
import ch.skaldenmagic.cardmarket.autopricing.domain.repository.PriceRepository;
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
 * Service to handle known Articles from the own MKM-Stock
 *
 * @author Kevin Zellweger
 * @Date 03.07.20
 */
@Service
@Transactional
public class ArticleService {

  private static final Logger LOGGER = LoggerFactory.getLogger(ArticleService.class);
  private final ArticlePriceMapper articlePriceMapper;
  private final MkmService mkmService;
  private final ArticleRepository articleRepository;
  private final PriceRepository priceRepository;
  private final ArticleMapper articleMapper;
  private final ProductService productService;
  private final ProductMapper productMapper;

  @Autowired
  public ArticleService(
      ArticlePriceMapper articlePriceMapper,
      MkmService mkmService,
      ArticleRepository articleRepository,
      PriceRepository priceRepository,
      ArticleMapper articleMapper,
      ProductService productService,
      ProductMapper productMapper) {
    this.articlePriceMapper = articlePriceMapper;
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

  public List<ArticleDto> findAllArticlesWithCheapestPriceByExpansion(String name)
      throws IOException {
    List<ProductEntity> productsByExpansionId = productService.findAllByExpansionName(name);
    List<Integer> productIds = productsByExpansionId.stream().map(ProductEntity::getProductId)
        .collect(Collectors.toList());
    List<ArticleEntity> byProductIds = articleRepository.findByProductIds(productIds);

    List<ArticleDto> articleDtos = byProductIds.stream().map(articleMapper::entityToDto)
        .collect(Collectors.toList());

    List<ArticlePriceEntity> byArticleId = priceRepository.findAll();
    Map<Integer, List<ArticlePriceEntity>> prices = byArticleId.stream()
        .collect(groupingBy(ArticlePriceEntity::getArticleId));

    List<ArticleDto> allWithPrices = getArticleDtosWithCheapestPrice(articleDtos, prices);
    return allWithPrices;
  }

  //FIXME
  public List<ArticleDto> findAllArticlesWithCheapestPriceByExpansion(Integer expansionId)
      throws IOException {
    List<ProductEntity> productsByExpansionId = productService.findAllByExpansionId(expansionId);
    List<Integer> productIds = productsByExpansionId.stream().map(ProductEntity::getProductId)
        .collect(Collectors.toList());
    List<ArticleEntity> byProductIds = articleRepository.findByProductIds(productIds);

    List<ArticleDto> articleDtos = byProductIds.stream().map(articleMapper::entityToDto)
        .collect(Collectors.toList());

    List<ArticleDto> allArticlesWithCheapestPriceByExpansion = new ArrayList<>();
    articleDtos.forEach(articleEntity -> {
      List<ArticlePriceEntity> byArticleId = priceRepository
          .findByArticleId(articleEntity.getArticleId());
      ArticlePriceEntity cheapestPrice = byArticleId.stream()
          .min(Comparator.comparing(ArticlePriceEntity::getPrice))
          .orElseThrow(NoSuchElementException::new);
      articleEntity.setArticlePriceEntity(articlePriceMapper.toDto(cheapestPrice));
      articleEntity.setPrice(cheapestPrice.getRecommendedPrice());
      allArticlesWithCheapestPriceByExpansion.add(articleEntity);
    });

    return allArticlesWithCheapestPriceByExpansion;
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

  public List<ArticleDto> findAllWithMinPrice() {
    //TODO optimization with Databasequeries or Entities with oneToMany
    List<ArticleEntity> articles = articleRepository.findAll();
    List<ArticleDto> articleDtos = articles.stream().map(articleMapper::entityToDto)
        .collect(Collectors.toList());
    List<ArticlePriceEntity> byArticleId = priceRepository.findAll();
    Map<Integer, List<ArticlePriceEntity>> prices = byArticleId.stream()
        .collect(groupingBy(ArticlePriceEntity::getArticleId));

    List<ArticleDto> allWithPrices = getArticleDtosWithCheapestPrice(articleDtos, prices);
    return allWithPrices;
  }

  public List<ArticleEntity> reloadStockFromMkm() throws IOException {
    articleRepository.deleteAll();
    List<Article> stock = mkmService.getCardMarket().getStockService().getStock();
    List<ArticleEntity> stockEntities = stock.stream().map(articleMapper::mkmToEntity)
        .collect(Collectors.toList());

    return updateArticleStock(stockEntities);
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

  public List<ArticleEntity> updateAll(List<ArticleDto> articleDtos) throws IOException {
    List<CardMarketArticle> entities = articleDtos.stream().map(articleMapper::dtoToMkm)
        .collect(Collectors.toList());
    List<Article> articles = mkmService.getCardMarket().getStockService()
        .editListArticles(entities);
    List<ArticleEntity> articleEntities = articles.stream().map(articleMapper::mkmToEntity)
        .collect(Collectors.toList());

    List<Integer> collect = articleEntities.stream().map(ArticleEntity::getArticleId)
        .collect(Collectors.toList());
    List<ArticleEntity> byArticleIds = articleRepository.findByArticleIds(collect);
    articleRepository.deleteInBatch(byArticleIds);

    List<ArticlePriceEntity> pricesByArticleIds = priceRepository.findByArticleIds(collect);
    priceRepository.deleteInBatch(pricesByArticleIds);

    return updateArticleStock(articleEntities);
  }

  private List<ArticleDto> getArticleDtosWithCheapestPrice(List<ArticleDto> articleDtos,
      Map<Integer, List<ArticlePriceEntity>> prices) {
    List<ArticleDto> allWithPrices = new ArrayList<>();
    articleDtos.forEach(articleEntity -> {
      if (null != prices.get(articleEntity.getArticleId())) {
        ArticlePriceEntity cheapestPrice = prices.get(articleEntity.getArticleId()).stream()
            .min(Comparator.comparing(ArticlePriceEntity::getPrice))
            .orElseThrow(NoSuchElementException::new);
        articleEntity.setArticlePriceEntity(articlePriceMapper.toDto(cheapestPrice));
        articleEntity.setPrice(cheapestPrice.getRecommendedPrice());
      }
      allWithPrices.add(articleEntity);
    });
    return allWithPrices;
  }

  private List<ArticleEntity> updateArticleStock(List<ArticleEntity> stockEntities) {
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

}
