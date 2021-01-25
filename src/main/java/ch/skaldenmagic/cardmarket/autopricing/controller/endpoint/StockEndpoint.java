package ch.skaldenmagic.cardmarket.autopricing.controller.endpoint;

import ch.skaldenmagic.cardmarket.autopricing.domain.entity.ArticleEntity;
import ch.skaldenmagic.cardmarket.autopricing.domain.entity.ExpansionEntity;
import ch.skaldenmagic.cardmarket.autopricing.domain.mapper.ArticleMapper;
import ch.skaldenmagic.cardmarket.autopricing.domain.mapper.dtos.ArticleDto;
import ch.skaldenmagic.cardmarket.autopricing.domain.service.ArticleService;
import ch.skaldenmagic.cardmarket.autopricing.domain.service.ExpansionServie;
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

  //FIXME
  @GetMapping("/articles/expansion/{id}")
  public List<ArticleDto> findAllArticlesWithCheapestPriceByExpansion(
      @PathVariable("id") Integer id) throws IOException {
    return articleService.findAllArticlesWithCheapestPriceByExpansion(id);
  }

  @GetMapping("/articles/expansion/name/{name}")
  public List<ArticleDto> findAllArticlesWithCheapestPriceByExpansionName(
      @PathVariable("name") String name) throws IOException {
    return articleService.findAllArticlesWithCheapestPriceByExpansion(name);
  }

  //TODO return DTO
  @GetMapping("/expansion/{name}")
  public List<ExpansionEntity> findExpansionsByName(@PathVariable("name") String name)
      throws IOException {
    return expansionService.findAllByNameContaining(name);
  }

  @GetMapping("/articles")
  public List<ArticleDto> loadMyStock() {
    //TODO: we have redundant entries in the database.... but why?
    List<ArticleEntity> articles = articleService.findAll();
    return articles.stream().map(articleMapper::entityToDto).collect(Collectors.toList());
  }

  @GetMapping("/articles/min-price")
  public List<ArticleDto> loadStockWithMinPrice() {
    return articleService.findAllWithMinPrice();
  }

  @PostMapping("/articles")
  public List<ArticleDto> updateMyStock(@RequestBody List<ArticleDto> articleDtos)
      throws IOException {
    return articleService.updateAll(articleDtos).stream().map(articleMapper::entityToDto)
        .collect(Collectors.toList());
  }

}
