package ch.softridge.cardmarket.autopricing.domain.service;

import ch.softridge.cardmarket.autopricing.domain.entity.ArticleEntity;
import ch.softridge.cardmarket.autopricing.domain.entity.ArticlePriceEntity;
import ch.softridge.cardmarket.autopricing.domain.entity.ExpansionEntity;
import ch.softridge.cardmarket.autopricing.domain.entity.ProductEntity;
import ch.softridge.cardmarket.autopricing.domain.mapper.ArticleMapper;
import ch.softridge.cardmarket.autopricing.domain.mapper.ExpansionMapper;
import ch.softridge.cardmarket.autopricing.domain.mapper.dtos.ArticleDto;
import ch.softridge.cardmarket.autopricing.domain.repository.ArticleRepository;
import ch.softridge.cardmarket.autopricing.domain.repository.ExpansionRepository;
import ch.softridge.cardmarket.autopricing.domain.repository.PriceRepository;
import ch.softridge.cardmarket.autopricing.domain.repository.ProductRepository;
import ch.softridge.cardmarket.autopricing.util.FileImport;
import de.cardmarket4j.entity.Expansion;
import de.cardmarket4j.entity.util.ProductFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProductService {

    private static final Logger log = LoggerFactory.getLogger(ProductService.class);
    @Autowired
    ArticleMapper articleMapper;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ArticleRepository articleRepository; //Todo: replace with service
    @Autowired
    private PriceRepository priceRepository; //Todo: replace with service

    @Autowired
    private MkmService mkmService;

    @Autowired
    private ExpansionRepository expansionRepository; //Todo: replace with service

    @Autowired
    private ExpansionMapper expansionMapper;

    public List<ArticleDto> findAllArticlesWithCheapestPriceByExpansion(Integer expansionId) throws IOException {
        List<ProductEntity> productsByExpansionId = productRepository.findAllByExpansionId(expansionId);
        List<Integer> productIds = productsByExpansionId.stream().map(ProductEntity::getProductId).collect(Collectors.toList());
        List<ArticleEntity> byProductIds = articleRepository.findByProductIds(productIds);

        List<ArticleDto> articleDtos = byProductIds.stream().map(articleMapper::articleEntityToDto).collect(Collectors.toList());

        List<ArticleDto> allArticlesWithCheapestPriceByExpansion = new ArrayList<>();
        articleDtos.forEach(articleEntity -> {
            List<ArticlePriceEntity> byArticleId = priceRepository.findByArticleId(articleEntity.getArticleId());
            ArticlePriceEntity cheapestPrice = byArticleId.stream().min(Comparator.comparing(ArticlePriceEntity::getPrice)).orElseThrow(NoSuchElementException::new);
            articleEntity.setArticlePriceEntity(cheapestPrice);
            articleEntity.setPrice(cheapestPrice.getRecommendedPrice());
            allArticlesWithCheapestPriceByExpansion.add(articleEntity);
        });

        return allArticlesWithCheapestPriceByExpansion;
    }

    public List<ExpansionEntity> findExpansionsByName(String expansionName) {
        return expansionRepository.findAllByNameContaining(expansionName);
    }

    public List<ExpansionEntity> persistExpansions() throws IOException {
        Set<Expansion> expansions = mkmService.getCardMarket().getMarketplaceService().getExpansions(new ProductFilter("?"));
        List<ExpansionEntity> entities = expansions.stream().map(expansionMapper::toEntity).collect(Collectors.toList());
        return expansionRepository.saveAll(entities);
    }

    @Deprecated
    public List<ProductEntity> persistProductFile() throws IOException {
        String encodedFileName = "MkmProductsfile.txt";
        String csvFileName = "MkmProductsfile.csv";
        productRepository.deleteAll();
        //TODO replace file with rest-request to mkm

        InputStream resourceAsStream = getClass().getClassLoader().getResourceAsStream(encodedFileName);
        byte[] zippedPriceArray = FileImport.decompressBase64(resourceAsStream);
        try (FileOutputStream fos = new FileOutputStream(csvFileName)) {
            fos.write(zippedPriceArray);
        }
        List<ProductEntity> productEntities = FileImport.readSorterProductCSV(csvFileName);
        return productRepository.saveAll(productEntities);
    }

    public List<ProductEntity> loadMkmProductlist(){
        String fileToImport = loadMkmProductListAsCSV();
        return null;
    }

    private String loadMkmProductListAsCSV() {
        String encodedFileName = "MkmProductsfile.txt";
        String csvFileName = "MkmProductsfile.csv";

        try {
            String content =
                    mkmService.getCardMarket().getMarketplaceService().getProductsFile().getAsJsonObject().get("productsfile").getAsString();

            InputStream inputStream =  new ByteArrayInputStream(content.getBytes());

            byte[] actualContent = FileImport.decompressBase64(inputStream);
            try (FileOutputStream fos = new FileOutputStream(csvFileName)) {
                fos.write(actualContent);
            }
        } catch (IOException e) {
            // TODO handle propper
        }
        return csvFileName;
    }

}
