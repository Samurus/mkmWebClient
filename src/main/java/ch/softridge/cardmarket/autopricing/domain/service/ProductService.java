package ch.softridge.cardmarket.autopricing.domain.service;

import ch.softridge.cardmarket.autopricing.domain.entity.ProductEntity;
import ch.softridge.cardmarket.autopricing.domain.mapper.ArticleMapper;
import ch.softridge.cardmarket.autopricing.domain.mapper.ProductMapper;
import ch.softridge.cardmarket.autopricing.domain.repository.ProductRepository;
import ch.softridge.cardmarket.autopricing.util.CSVUtil;
import ch.softridge.cardmarket.autopricing.util.FileImport;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class ProductService {

  private static final Logger log = LoggerFactory.getLogger(ProductService.class);
  @Autowired
  ArticleMapper articleMapper;
  @Autowired
  private ProductRepository productRepository;
  @Autowired
  private MkmService mkmService;
  @Autowired
  private ExpansionServie expansionService;

  @Autowired
  private ArticleService articleService;

  @Autowired
  ProductMapper productMapper;


  /**
   * Load all Stored Products by Expansion
   *
   * @param expansionId Id of desired Expansion
   * @return List of found Products
   */
  public List<ProductEntity> findAllByExpansionId(Integer expansionId) {
    return productRepository.findAllByExpansionId(expansionId);
  }

  List<ProductEntity> findByProductIdInList(List<Integer> productIds) {
    return productRepository.findByProductIdInList(productIds);
  }

  /**
   * Load all Products from MKM-API. Only Products which have a newer added Date than the ones
   * already persisted will be stored. Be aware that this function might take some while and only a
   * minimal Set of information is provided from the MKM-API.
   */
  public void loadMkmProductlist() {
    try (BufferedReader reader = new BufferedReader(
        new InputStreamReader(new ByteArrayInputStream(loadMkmProductListAsCSV())))) {
      LocalDate mostRecentDate = getMostRecentDate();
      List<ProductEntity> entities = reader.lines()
          .skip(1)
          .map(line -> parseProductCSV(CSVUtil.parseLine(line)))
          .filter(productEntity -> productEntity.getDateAdded().isAfter(mostRecentDate))
          .collect(Collectors.toList());

      updateAllProducts(entities);
    } catch (IOException e) {
      log.error(e.getMessage());
    }
  }

  /**
   * Uses a preloaded file
   *
   * @return List of persisted ProductEntitys
   * @throws IOException
   * @deprecated use ProductSerive.loadMkmProductlist() instead
   */
  @Deprecated
  public List<ProductEntity> persistProductFile() throws IOException {
    String encodedFileName = "MkmProductsfile.txt";
    String csvFileName = "MkmProductsfile.csv";

    InputStream resourceAsStream = getClass().getClassLoader().getResourceAsStream(encodedFileName);
    byte[] zippedPriceArray = FileImport.getProductsFile(resourceAsStream);
    try (FileOutputStream fos = new FileOutputStream(csvFileName)) {
      fos.write(zippedPriceArray);
    }
    List<ProductEntity> productEntities = FileImport.readSorterProductCSV(csvFileName);

    return updateAllProducts(productEntities);
  }

  private List<ProductEntity> updateAllProducts(List<ProductEntity> productEntities) {
    Map<Integer, ProductEntity> existingProducts = productRepository.findAll().stream()
        .collect(Collectors.toMap(ProductEntity::getProductId, Function.identity()));

    productEntities.forEach(newProduct -> {
      ProductEntity existingProduct = existingProducts.get(newProduct.getProductId());
      if (null != existingProduct) {
        productMapper.updateSecondWithFirst(existingProduct, newProduct);
      }
    });

    return productRepository.saveAll(productEntities);
  }

  private LocalDate getMostRecentDate() {
    LocalDate mostRecentDate = productRepository.getMostRecentDateAdded();
    if (mostRecentDate == null) {
      mostRecentDate = LocalDate.MIN;
    }
    return mostRecentDate;
  }

  private byte[] loadMkmProductListAsCSV() {
    try {
      String content =
          mkmService.getCardMarket().getMarketplaceService().getProductsFile().getAsJsonObject()
              .get("productsfile").getAsString();
      InputStream inputStream = new ByteArrayInputStream(content.getBytes());
      return FileImport.decompressBase64(inputStream);
    } catch (IOException e) {
      log.error(e.getMessage());
      return new byte[0];
    }
  }

  private ProductEntity parseProductCSV(List<String> inLine) {
    ProductEntity entity = new ProductEntity();
    entity.setProductId(CSVUtil.parseIntegerColumn(inLine.get(0)));
    entity.setName(CSVUtil.parseStringColumn(inLine.get(1)));
    entity.setCategoryId(CSVUtil.parseIntegerColumn(inLine.get(2)));
    entity.setCategoryName(CSVUtil.parseStringColumn(inLine.get(3)));
    entity
        .setExpansion(expansionService.getByExpansionId(CSVUtil.parseIntegerColumn(inLine.get(4))));
    entity.setMetaproductId(CSVUtil.parseIntegerColumn(inLine.get(5)));
    try {
      entity.setDateAdded(CSVUtil.parseDateColumn(inLine.get(6)));
    } catch (DateTimeParseException e) {
      log.error(e.getMessage());
    }
    return entity;
  }

  public ProductEntity saveNewProduct(ProductEntity product) {
    return productRepository.save(product);
  }

  public List<ProductEntity> findAllByExpansionName(String name) {
    return productRepository.findAllByExpansionNameContaining(name);
  }

  public List<String> findAllExpansionNames() {
    int magicSingleCode = 1;
    return productRepository.findExpansionNamesDistinctByCategoryId(magicSingleCode).stream()
        .filter(Objects::nonNull)
        .collect(Collectors.toList());
  }
}
