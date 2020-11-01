package ch.softridge.cardmarket.autopricing.domain.service;

import ch.softridge.cardmarket.autopricing.domain.mapper.dtos.ArticleDto;
import ch.softridge.cardmarket.autopricing.domain.entity.ArticlePriceEntity;
import ch.softridge.cardmarket.autopricing.domain.repository.ArticleRepository;
import ch.softridge.cardmarket.autopricing.domain.repository.PriceRepository;
import ch.softridge.cardmarket.autopricing.domain.entity.ArticleEntity;
import ch.softridge.cardmarket.autopricing.domain.mapper.ArticleMapper;
import de.cardmarket4j.entity.Article;
import de.cardmarket4j.entity.CardMarketArticle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Kevin Zellweger
 * @Date 03.07.20
 */
@Service
@Transactional
public class ArticleService {

    private static Logger log = LoggerFactory.getLogger(ArticleService.class);

    @Autowired
    private MkmService mkmService;

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private PriceRepository priceRepository;

    @Autowired
    private ArticleMapper articleMapper;


    public List<ArticleEntity> reloadStockFromMkm() throws IOException {
        articleRepository.deleteAll();
        List<Article> stock = mkmService.getCardMarket().getStockService().getStock();
        List<ArticleEntity> entities = stock.stream().map(articleMapper::apiArticleToEntity).collect(Collectors.toList());
        return articleRepository.saveAll(entities);
    }


    public List<ArticleEntity> saveAll(List<ArticleEntity> articleEntities){
        return articleRepository.saveAll(articleEntities);
    }


    public List<ArticleEntity> updateAll(List<ArticleDto> articleDtos) throws IOException {
        List<CardMarketArticle> entities = articleDtos.stream().map(articleMapper::dtoToArticle).collect(Collectors.toList());
        List<Article> articles = mkmService.getCardMarket().getStockService().editListArticles(entities);
        List<ArticleEntity> articleEntities = articles.stream().map(articleMapper::apiArticleToEntity).collect(Collectors.toList());

        List<Integer> collect = articleEntities.stream().map(ArticleEntity::getArticleId).collect(Collectors.toList());
        List<ArticleEntity> byArticleIds = articleRepository.findByArticleIds(collect);
        articleRepository.deleteInBatch(byArticleIds);
        return articleRepository.saveAll(articleEntities);
    }

    public List<ArticleDto> findAllWithMinPrice(){
        //TODO optimization with Databasequeries or Entities with oneToMany
        List<ArticleEntity> articles = articleRepository.findAll();
        List<ArticleDto> articleDtos = articles.stream().map(articleMapper::articleEntityToDto).collect(Collectors.toList());
        List<ArticlePriceEntity> byArticleId = priceRepository.findAll();
        Map<Integer, List<ArticlePriceEntity>> prices = byArticleId.stream().collect(Collectors.groupingBy(ArticlePriceEntity::getArticleId));

        List<ArticleDto> allWithPrices = new ArrayList<>();
        articleDtos.forEach(articleEntity -> {
            ArticlePriceEntity cheapestPrice = prices.get(articleEntity.getArticleId()).stream().min(Comparator.comparing(ArticlePriceEntity::getPrice)).orElseThrow(NoSuchElementException::new);
            articleEntity.setArticlePriceEntity(cheapestPrice);
            articleEntity.setPrice(cheapestPrice.getRecommendedPrice());
            allWithPrices.add(articleEntity);
        });

        return allWithPrices;

    }

    public List<ArticleEntity> findAll(){
        return articleRepository.findAll();
    }

    public void deleteAll(){
        articleRepository.deleteAll();
    }

}
