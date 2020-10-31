package ch.softridge.cardmarket.autopricing.service;

import ch.softridge.cardmarket.autopricing.controller.model.ArticleDto;
import ch.softridge.cardmarket.autopricing.repository.ArticleRepository;
import ch.softridge.cardmarket.autopricing.repository.ExpansionRepository;
import ch.softridge.cardmarket.autopricing.repository.PriceRepository;
import ch.softridge.cardmarket.autopricing.repository.ProductRepository;
import ch.softridge.cardmarket.autopricing.repository.model.ArticleEntity;
import ch.softridge.cardmarket.autopricing.repository.model.ArticlePrice;
import ch.softridge.cardmarket.autopricing.repository.model.ExpansionEntity;
import ch.softridge.cardmarket.autopricing.repository.model.ProductEntity;
import ch.softridge.cardmarket.autopricing.service.mapper.ArticleMapper;
import ch.softridge.cardmarket.autopricing.service.mapper.ExpansionMapper;
import ch.softridge.cardmarket.autopricing.service.util.FileImport;
import de.cardmarket4j.entity.Expansion;
import de.cardmarket4j.entity.util.ProductFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProductService {

    private static Logger log = LoggerFactory.getLogger(ProductService.class);

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    ArticleMapper articleMapper;

    @Autowired
    private PriceRepository priceRepository;

    @Autowired
    private MkmService mkmService;

    @Autowired
    private ExpansionRepository expansionRepository;

    @Autowired
    private ExpansionMapper expansionMapper;

    public List<ProductEntity> persistProductFile() throws IOException {
        productRepository.deleteAll();
        //TODO replace file with rest-request to mkm
        InputStream resourceAsStream = getClass().getClassLoader().getResourceAsStream("MkmProductsfile.txt");
        byte[] zippedPriceArray = FileImport.getProductsFile(resourceAsStream);
        try (FileOutputStream fos = new FileOutputStream("MkmProductsfile.csv")) {
            fos.write(zippedPriceArray);
        }
        List<ProductEntity> productEntities = FileImport.readSorterProductCSV("MkmProductsfile.csv");
        return productRepository.saveAll(productEntities);
    }


    public List<ExpansionEntity> persistExpansions() throws IOException {
        Set<Expansion> expansions = mkmService.getCardMarket().getMarketplaceService().getExpansions(new ProductFilter("?"));
        List<ExpansionEntity> entities = expansions.stream().map(expansionMapper::toEntity).collect(Collectors.toList());
        return expansionRepository.saveAll(entities);
    }

    public List<ExpansionEntity> findExpansionsByName(String expansionName) {
        return expansionRepository.findAllByNameContaining(expansionName);
    }

    public List<ArticleDto> findAllArticlesWithCheapestPriceByExpansion(Integer expansionId) throws IOException {
        List<ProductEntity> productsByExpansionId = productRepository.findAllByExpansionId(expansionId);
        List<Integer> productIds = productsByExpansionId.stream().map(ProductEntity::getProductId).collect(Collectors.toList());
        List<ArticleEntity> byProductIds = articleRepository.findByProductIds(productIds);

        List<ArticleDto> articleDtos = byProductIds.stream().map(articleMapper::articleEntityToDto).collect(Collectors.toList());

        List<ArticleDto> allArticlesWithCheapestPriceByExpansion = new ArrayList<>();
        articleDtos.forEach(articleEntity -> {
            List<ArticlePrice> byArticleId = priceRepository.findByArticleId(articleEntity.getArticleId());
            ArticlePrice cheapestPrice = byArticleId.stream().min(Comparator.comparing(ArticlePrice::getPrice)).orElseThrow(NoSuchElementException::new);
            articleEntity.setArticlePrice(cheapestPrice);
            articleEntity.setPrice(cheapestPrice.getRecommendedPrice());
            allArticlesWithCheapestPriceByExpansion.add(articleEntity);
        });

        return allArticlesWithCheapestPriceByExpansion;
    }

}
