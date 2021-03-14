package ch.skaldenmagic.cardmarket.autopricing.domain.service;

import ch.skaldenmagic.cardmarket.autopricing.domain.entity.ExpansionEntity;
import ch.skaldenmagic.cardmarket.autopricing.domain.entity.LocalizationEntity;
import ch.skaldenmagic.cardmarket.autopricing.domain.entity.MkmPriceGuide;
import ch.skaldenmagic.cardmarket.autopricing.domain.entity.ProductEntity;
import ch.skaldenmagic.cardmarket.autopricing.domain.mapper.LocalizationMapper;
import ch.skaldenmagic.cardmarket.autopricing.domain.mapper.ProductMapper;
import ch.skaldenmagic.cardmarket.autopricing.domain.mapper.dtos.ArticleDto;
import ch.skaldenmagic.cardmarket.autopricing.domain.mapper.dtos.ProductDto;
import ch.skaldenmagic.cardmarket.autopricing.domain.model.Card;
import ch.skaldenmagic.cardmarket.autopricing.domain.repository.ProductRepository;
import ch.skaldenmagic.cardmarket.autopricing.domain.service.exceptions.MkmAPIException;
import ch.skaldenmagic.cardmarket.autopricing.util.CSVUtil;
import ch.skaldenmagic.cardmarket.autopricing.util.FileImport;
import com.neovisionaries.i18n.LanguageCode;
import de.cardmarket4j.entity.Product;
import de.cardmarket4j.entity.enumeration.Condition;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
  private final ProductMapper productMapper;
  private final LocalizationMapper localizationMapper;
  private final ProductRepository productRepository;
  private final MkmService mkmService;
  private final ExpansionServie expansionService;
  private final MkmPriceGuideService priceGuideService;

  @Autowired
  public ProductService(
      ProductMapper productMapper,
      LocalizationMapper localizationMapper,
      ProductRepository productRepository,
      MkmService mkmService,
      ExpansionServie expansionService,
      MkmPriceGuideService priceGuideService) {
    this.productMapper = productMapper;
    this.localizationMapper = localizationMapper;
    this.productRepository = productRepository;
    this.mkmService = mkmService;
    this.expansionService = expansionService;
    this.priceGuideService = priceGuideService;
  }

  public void deleteAll() {
    productRepository.deleteAll();
  }

  /**
   * Load all Stored Products by Expansion
   *
   * @param expansionId Id of desired Expansion
   * @return List of found Products
   */
  public List<ProductEntity> findAllByExpansionId(Integer expansionId) {
    return productRepository.findAllByExpansionId(expansionId);
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

  public Optional<ProductEntity> findByNameAndExpansion(String name, Long expansionId) {
    return Optional.ofNullable(productRepository.findByNameAndExpansionId(name, expansionId));
  }

  /**
   * Find Product by MKM-ProductId
   *
   * @param productId MKM-ProductID
   * @return ProductEntity
   */

  public Optional<ProductEntity> findByProductId(Integer productId) {
    return Optional.ofNullable(productRepository.findByProductId(productId));
  }

  public List<ArticleDto> getFromSorterData(List<Card> sorterCards) {
    List<ArticleDto> result = new ArrayList<>();
    ArticleDto unknown = new ArticleDto();
    ProductDto unkonwProduct = new ProductDto();
    unkonwProduct.setName("Unknown Card");
    unkonwProduct.setImageUrl("./img/items/1/WAR/371876.jpg");
    unknown.setComment("Unkwon Article");
    unknown.setCondition(Condition.POOR);
    unknown.setLastEdited(LocalDateTime.now());
    unknown.setProduct(unkonwProduct);
    unknown.setQuantity(0);
    unknown.setPrice(BigDecimal.ZERO);
    for (Card c : sorterCards) {
      ExpansionEntity expansion = expansionService.getByCode(c.getSet());
      if (expansion != null) {
        Optional<ProductEntity> product = findByNameAndExpansion(c.getTitle(), expansion.getId());
        if (product.isPresent()) {
          ProductDto productDto = productMapper.entityToDto(product.get());
          result.add(defaultArticleDTO(c, productDto));
        } else {
          unknown.setQuantity(unknown.getQuantity() + 1);
        }
      }
    }
    if (unknown.getQuantity() > 0) {
      result.add(unknown);
    }
    return result;
  }

  public void initProductDatabase() {
    try {
      List<ExpansionEntity> expansions = expansionService.updateExpansionDB();
      //TODO: init Productfilter (and use it accurate) only for unknown expansions -> flush transactions as well manualy
      List<ProductEntity> mkmProducts = new ArrayList<>();
      for (ExpansionEntity expansion : expansions) {
        Set<Product> apiRes = mkmService.getCardMarket().getMarketplaceService()
            .getExpansionSingles(expansion.getExpansionId());
        for (Product p : apiRes) {
          ProductEntity e = productMapper.mkmToEntity(p);
          Set<LocalizationEntity> localizations = localizationMapper
              .mapToLocalization(p.getMapLocalizedNames(), e);
          e.setLocalizations(localizations);
          e.setExpansion(expansion);
          mkmProducts.add(e);
        }
      }
      productRepository.saveAll(mkmProducts);
      productRepository.flush();
    } catch (IOException e) {
      //Todo handle exeptions correctly
    }
  }

  public Optional<? extends ProductEntity> loadMkmProduct(Integer productId) {
    try {
      Product product = mkmService.getCardMarket().getMarketplaceService()
          .getProductDetails(productId);
      ProductEntity e = productMapper.mkmToEntity(product);
      if (product.getPriceGuide() != null) {
        MkmPriceGuide mkmPriceGuide = new MkmPriceGuide();

        mkmPriceGuide.setSell(product.getPriceGuide().getSell());
        mkmPriceGuide.setLow(product.getPriceGuide().getLow());
        mkmPriceGuide.setLowExPlus(product.getPriceGuide().getLowExPlus());
        mkmPriceGuide.setLowFoil(product.getPriceGuide().getLowFoil());
        mkmPriceGuide.setAvg(product.getPriceGuide().getAvg());
        mkmPriceGuide.setTrend(product.getPriceGuide().getTrend());

        mkmPriceGuide = priceGuideService.save(mkmPriceGuide);
        e.setPriceGuide(mkmPriceGuide);
      }
      Set<LocalizationEntity> localizations = localizationMapper
          .mapToLocalization(product.getMapLocalizedNames(), e);
      if (product.getExpansion() != null) {
        ExpansionEntity expansionEntity = expansionService
            .getByExpansionId(product.getExpansion().getExpansionId());
        if (expansionEntity == null) {
          expansionEntity = new ExpansionEntity();
          expansionEntity.setName(product.getExpansionName());
          expansionEntity.setCode(product.getExpansion().getCode());
          expansionEntity.setExpansionId(product.getExpansion().getExpansionId());
          expansionService.save(expansionEntity);
        }
        e.setExpansion(expansionEntity);
      }

      e.setLocalizations(localizations);

      e = productRepository.saveAndFlush(e);
      return Optional.of(e);
    } catch (IOException e) {
      throw new MkmAPIException(ProductService.class, "failed load single Product: ",
          productId.toString());
    }
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

  public ProductEntity saveNewProduct(ProductEntity product) {
    return productRepository.save(product);
  }

  private ArticleDto defaultArticleDTO(Card c, ProductDto productDto) {
    if (c.getPrice() < 0.02) {
      c.setPrice(0.02); //Respect Cardmarkets minimum Price
    }
    return new ArticleDto(null, LanguageCode.en, "Added By Sorter",
        BigDecimal.valueOf(c.getPrice()), c.getCount(), false, productDto, null,
        LocalDateTime.now(), Condition.NEAR_MINT, false, false, false, false, false, null);
  }

  private List<ProductEntity> findAllByName(String name) {
    return productRepository.findAllByName(name);
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

  List<ProductEntity> findByProductIdInList(List<Integer> productIds) {
    return productRepository.findByProductIdInList(productIds);
  }

}
