package ch.skaldenmagic.cardmarket.autopricing.controller.endpoint;

import ch.skaldenmagic.cardmarket.autopricing.domain.entity.ArticleEntity;
import ch.skaldenmagic.cardmarket.autopricing.domain.entity.ArticlePriceEntity;
import ch.skaldenmagic.cardmarket.autopricing.domain.entity.ExpansionEntity;
import ch.skaldenmagic.cardmarket.autopricing.domain.mapper.ArticleMapper;
import ch.skaldenmagic.cardmarket.autopricing.domain.mapper.dtos.ArticleDto;
import ch.skaldenmagic.cardmarket.autopricing.domain.service.ArticleService;
import ch.skaldenmagic.cardmarket.autopricing.domain.service.ExpansionServie;
import ch.skaldenmagic.cardmarket.autopricing.domain.service.PriceService;
import ch.skaldenmagic.cardmarket.autopricing.domain.service.ProductService;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/reload")
public class ReloadEndpoint {

  private static final Logger log = LoggerFactory.getLogger(ReloadEndpoint.class);

  @Autowired
  private PriceService priceService;

  @Autowired
  private ArticleService articleService;

  @Autowired
  private ProductService productService;

  @Autowired
  private ArticleMapper articleMapper;

  @Autowired
  private ExpansionServie expansionService;


  //TODO return DTO
  @GetMapping("/prices/{name}")
  public List<ArticlePriceEntity> reloadPricesRecommendations(@PathVariable("name") String name)
      throws IOException {
    priceService.reloadPricesForUser(name);
    return priceService.reloadPricesRecommendations(name);
  }

  @GetMapping("/stock")
  public List<ArticleDto> reloadMyStockFromMkm(Model model) {
    try {
      List<ArticleEntity> articles = articleService.reloadStockFromMkm();
      return articles.stream().map(articleMapper::entityToDto).collect(Collectors.toList());
    } catch (IOException e) {
      //TODO throw mkm-exception and handle with user. g.g. https://www.baeldung.com/exception-handling-for-rest-with-spring
      e.printStackTrace();
    }
    return new ArrayList<>();
  }


  @GetMapping("/products")
  public void persistProductFile() throws IOException {
    productService.loadMkmProductlist();
  }

  @GetMapping("/products/file")
  @Deprecated
  public void persistProductFileFromFile() throws IOException {
    productService.persistProductFile();
  }


  //TODO return DTO
  @GetMapping("/expansions")
  public List<ExpansionEntity> reloadExpansions() throws IOException {
    try {
      return expansionService.persistExpansions();
    } catch (Exception e) {
      log.error(e.getMessage() + Arrays.toString(e.getStackTrace()));
      return new ArrayList<>();
    }
  }
}
