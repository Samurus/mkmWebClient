package ch.skaldenmagic.cardmarket.autopricing.domain.service;

import ch.skaldenmagic.cardmarket.autopricing.domain.entity.ExpansionEntity;
import ch.skaldenmagic.cardmarket.autopricing.domain.entity.ProductEntity;
import ch.skaldenmagic.cardmarket.autopricing.domain.mapper.ArticleMapper;
import ch.skaldenmagic.cardmarket.autopricing.domain.mapper.ProductMapper;
import ch.skaldenmagic.cardmarket.autopricing.domain.model.Card;
import ch.skaldenmagic.cardmarket.autopricing.domain.repository.ProductRepository;
import ch.skaldenmagic.cardmarket.autopricing.util.CSVUtil;
import ch.skaldenmagic.cardmarket.autopricing.util.FileImport;
import de.cardmarket4j.entity.Product;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
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
  ProductMapper productMapper;
  @Autowired
  private ProductRepository productRepository;
  @Autowired
  private MkmService mkmService;
  @Autowired
  private ExpansionServie expansionService;
  @Autowired
  private ArticleService articleService;

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

  public List<ProductEntity> getFromSorterData(List<Card> sorterCards) {
    List<ProductEntity> obviousProducts = new ArrayList<>();
    List<ProductEntity> ambiguousProducts = new ArrayList<>();
    List<Card> unknownProducts = new ArrayList<>();

    for (Card c : sorterCards) {
      ExpansionEntity expansion = expansionService.getByCode(c.getSet());
      if (expansion != null) {
        Optional<ProductEntity> product = findByNameAndExpansion(c.getTitle(), expansion.getId());
        if (product.isPresent()) {
          obviousProducts.add(product.get());
        } else {
          unknownProducts.add(c);
        }
      } else {
        List<ProductEntity> possibleProducts = findAllByName(c.getTitle());
        if (!possibleProducts.isEmpty()) {
          ambiguousProducts.addAll(possibleProducts);
        } else {
          unknownProducts.add(c);
        }
      }
    }
    return obviousProducts;
  }

  public Optional<ProductEntity> findByNameAndExpansion(String name, Long expansionId) {
    return Optional.ofNullable(productRepository.findByNameAndExpansionId(name, expansionId));
  }

  private List<ProductEntity> findAllByName(String name) {
    return productRepository.findAllByName(name);
  }

  public void deleteAll() {
    productRepository.deleteAll();
  }

  public void initProductDatabase() {
    try {
      expansionService.updateExpansionDB();
      List<ExpansionEntity> expansions = expansionService.findAll();

      List<ProductEntity> mkmProducts = new ArrayList<>();
      for (ExpansionEntity expansion : expansions) {
        Set<Product> apiRes = mkmService.getCardMarket().getMarketplaceService()
            .getExpansionSingles(expansion.getExpansionId());
        for (Product p : apiRes) {
          ProductEntity e = productMapper.mkmToEntity(p);
          e.setExpansion(expansion);
          mkmProducts.add(e);
        }
      }
      productRepository.saveAll(mkmProducts);
    } catch (IOException e) {
      //Todo handle exeptions correctly
    }
  }
}
