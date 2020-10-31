package ch.softridge.cardmarket.autopricing.domain.service;

import ch.softridge.cardmarket.autopricing.domain.entity.ArticleEntity;
import ch.softridge.cardmarket.autopricing.domain.entity.ArticlePriceEntity;
import ch.softridge.cardmarket.autopricing.domain.entity.ProductEntity;
import ch.softridge.cardmarket.autopricing.domain.mapper.ArticleMapper;
import ch.softridge.cardmarket.autopricing.domain.mapper.dtos.ArticleDto;
import ch.softridge.cardmarket.autopricing.domain.repository.ArticleRepository;
import ch.softridge.cardmarket.autopricing.domain.repository.PriceRepository;
import ch.softridge.cardmarket.autopricing.domain.repository.ProductRepository;
import ch.softridge.cardmarket.autopricing.util.CSVUtil;
import ch.softridge.cardmarket.autopricing.util.FileImport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
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
    private ExpansionServie expansionService;

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

    public List<ProductEntity> loadMkmProductlist() {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(loadMkmProductListAsCSV())))) {
            LocalDate mostRecentDate = getMostRecentDate();
            List<ProductEntity> entities = reader.lines()
                    .skip(1)
                    .map(line -> parseProductCSV(CSVUtil.parseLine(line)))
                    .filter(productEntity ->productEntity.getDateAdded().isAfter(mostRecentDate))
                    .collect(Collectors.toList());

            productRepository.saveAll(entities);
        } catch (IOException e) {
            log.error(e.getMessage());
        }

        return null;
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

    private byte[] loadMkmProductListAsCSV() {
        try {
            String content =
                    mkmService.getCardMarket().getMarketplaceService().getProductsFile().getAsJsonObject().get("productsfile").getAsString();
            InputStream inputStream = new ByteArrayInputStream(content.getBytes());
            return FileImport.decompressBase64(inputStream);
        } catch (IOException e) {
            log.error(e.getMessage());
            //TODO introduce API error handler https://www.toptal.com/java/spring-boot-rest-api-error-handling
            return new byte[0];
        }
    }

    private ProductEntity parseProductCSV(List<String> inLine) {

        ProductEntity entity = new ProductEntity();
        entity.setProductId(CSVUtil.parseIntegerColumn(inLine.get(0)));
        entity.setName(CSVUtil.parseStringColumn(inLine.get(1)));
        entity.setCategoryId(CSVUtil.parseIntegerColumn(inLine.get(2)));
        entity.setCategoryName(CSVUtil.parseStringColumn(inLine.get(3)));
        entity.setExpansion(expansionService.getByExpansionId(CSVUtil.parseIntegerColumn(inLine.get(4))));
        entity.setMetaproductId(CSVUtil.parseIntegerColumn(inLine.get(5)));
        try {
            entity.setDateAdded(CSVUtil.parseDateColumn(inLine.get(6)));
        } catch (DateTimeParseException e){
            log.error(e.getMessage());
        }
        return entity;
    }

    private LocalDate getMostRecentDate(){
        LocalDate mostRecentDate = productRepository.getMostRecentDateAdded();
        if(mostRecentDate == null){
            mostRecentDate = LocalDate.MIN;
        }
        return mostRecentDate;
    }
}
