package ch.softridge.cardmarket.autopricing.service;

import ch.softridge.cardmarket.autopricing.repository.ArticleRepository;
import ch.softridge.cardmarket.autopricing.repository.model.ArticleEntity;
import ch.softridge.cardmarket.autopricing.service.mapper.ArticleMapper;
import de.cardmarket4j.entity.Article;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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
    private ArticleMapper articleMapper;


    public List<ArticleEntity> reloadStockFromMkm() throws IOException {
        articleRepository.deleteAll();
        List<Article> stock = mkmService.getCardMarket().getStockService().getStock();
        List<ArticleEntity> entities = stock.stream().map(articleMapper::articleToEntity).collect(Collectors.toList());
        return articleRepository.saveAll(entities);
    }


    public List<ArticleEntity> saveAll(List<ArticleEntity> articleEntities){
        return articleRepository.saveAll(articleEntities);
    }


    public List<ArticleEntity> findAll(){
        return articleRepository.findAll();
    }

    public void deleteAll(){
        articleRepository.deleteAll();
    }

}
