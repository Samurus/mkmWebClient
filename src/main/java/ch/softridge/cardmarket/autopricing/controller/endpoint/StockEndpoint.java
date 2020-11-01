package ch.softridge.cardmarket.autopricing.controller.endpoint;

import ch.softridge.cardmarket.autopricing.domain.entity.ArticleEntity;
import ch.softridge.cardmarket.autopricing.domain.entity.ExpansionEntity;
import ch.softridge.cardmarket.autopricing.domain.mapper.ArticleMapper;
import ch.softridge.cardmarket.autopricing.domain.mapper.dtos.ArticleDto;
import ch.softridge.cardmarket.autopricing.domain.service.ArticleService;
import ch.softridge.cardmarket.autopricing.domain.service.ExpansionServie;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Kevin Zellweger
 * @Date 05.07.20
 */
@RestController
@RequestMapping("/stock")
public class StockEndpoint {

  @Autowired
  private ArticleService articleService;

  @Autowired
  private ArticleMapper articleMapper;

  @Autowired
  private ExpansionServie expansionService;

  @GetMapping("/articles")
  public List<ArticleDto> loadMyStock() {
    List<ArticleEntity> articles = articleService.findAll();
    return articles.stream().map(articleMapper::articleEntityToDto).collect(Collectors.toList());
  }

  @PostMapping("/articles")
  public List<ArticleEntity> updateMyStock(@RequestBody List<ArticleDto> articleDtos)
      throws IOException {
    return articleService.updateAll(articleDtos);
  }

  @GetMapping("/articles/min-price")
  public List<ArticleDto> loadStockWithMinPrice() {
    return articleService.findAllWithMinPrice();
  }

  @GetMapping("/expansion/{name}")
  public List<ExpansionEntity> findExpansionsByName(@PathVariable("name") String name)
      throws IOException {
    return expansionService.findAllByNameContaining(name);
  }

  @GetMapping("/articles/expansion/{id}")
  public List<ArticleDto> findAllArticlesWithCheapestPriceByExpansion(
      @PathVariable("id") Integer id) throws IOException {
    return articleService.findAllArticlesWithCheapestPriceByExpansion(id);
  }

}
