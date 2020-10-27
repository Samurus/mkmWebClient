package ch.softridge.cardmarket.autopricing.service;

import ch.softridge.cardmarket.autopricing.repository.ArticleRepository;
import ch.softridge.cardmarket.autopricing.repository.ExpansionRepository;
import ch.softridge.cardmarket.autopricing.repository.ProductRepository;
import ch.softridge.cardmarket.autopricing.repository.model.ArticleEntity;
import ch.softridge.cardmarket.autopricing.repository.model.ExpansionEntity;
import ch.softridge.cardmarket.autopricing.repository.model.ProductEntity;
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
import java.util.List;
import java.util.Set;
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

    public List<ProductEntity> findProductsByExpansionId(Integer expansionId) throws IOException {
        return productRepository.findAllByExpansionId(expansionId);
    }


    public List<ExpansionEntity> findExpansionsByName(String expansionName){
        return expansionRepository.findAllByNameContaining(expansionName);
    }

    public List<ArticleEntity> findArticleByExpansionId(Integer expansionId) throws IOException {
        List<ProductEntity> productsByExpansionId = findProductsByExpansionId(expansionId);
        List<Integer> productIds = productsByExpansionId.stream().map(ProductEntity::getProductId).collect(Collectors.toList());
        return articleRepository.findByProductIds(productIds);
    }

}
