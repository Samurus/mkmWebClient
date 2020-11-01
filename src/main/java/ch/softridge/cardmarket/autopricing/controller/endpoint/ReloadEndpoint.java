package ch.softridge.cardmarket.autopricing.controller.endpoint;

import ch.softridge.cardmarket.autopricing.domain.entity.ArticleEntity;
import ch.softridge.cardmarket.autopricing.domain.entity.ArticlePriceEntity;
import ch.softridge.cardmarket.autopricing.domain.entity.ExpansionEntity;
import ch.softridge.cardmarket.autopricing.domain.entity.ProductEntity;
import ch.softridge.cardmarket.autopricing.domain.mapper.ArticleMapper;
import ch.softridge.cardmarket.autopricing.domain.mapper.dtos.ArticleDto;
import ch.softridge.cardmarket.autopricing.domain.service.ArticleService;
import ch.softridge.cardmarket.autopricing.domain.service.ExpansionServie;
import ch.softridge.cardmarket.autopricing.domain.service.PriceService;
import ch.softridge.cardmarket.autopricing.domain.service.ProductService;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/reload")
public class ReloadEndpoint {

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


  @GetMapping("/prices/{name}")
  public List<ArticlePriceEntity> reloadPricesRecommendations(@PathVariable("name") String name)
      throws IOException {
    return priceService.reloadPricesRecommendations(name);
  }

  @GetMapping("/stock")
  public List<ArticleDto> reloadMyStockFromMkm(Model model) {
    try {
      List<ArticleEntity> articles = articleService.reloadStockFromMkm();
      return articles.stream().map(articleMapper::articleEntityToDto).collect(Collectors.toList());
    } catch (IOException e) {
      //TODO throw mkm-exception and handle with user. g.g. https://www.baeldung.com/exception-handling-for-rest-with-spring
      e.printStackTrace();
    }
    return new ArrayList<>();
  }


  @GetMapping("/products")
  public List<ProductEntity> persistProductFile() throws IOException {
    return productService.loadMkmProductlist();
  }


  @GetMapping("/expansions")
  public List<ExpansionEntity> reloadExpansions() throws IOException {
    return expansionService.persistExpansions();
  }
}
