package ch.skaldenmagic.cardmarket.autopricing.controller;

import ch.skaldenmagic.cardmarket.autopricing.domain.entity.ArticleEntity;
import ch.skaldenmagic.cardmarket.autopricing.domain.entity.ExpansionEntity;
import ch.skaldenmagic.cardmarket.autopricing.domain.mapper.ArticleMapper;
import ch.skaldenmagic.cardmarket.autopricing.domain.mapper.dtos.ArticleDto;
import ch.skaldenmagic.cardmarket.autopricing.domain.service.ArticleService;
import ch.skaldenmagic.cardmarket.autopricing.domain.service.ExpansionServie;
import ch.skaldenmagic.cardmarket.autopricing.domain.service.StockService;
import ch.skaldenmagic.cardmarket.autopricing.domain.service.exceptions.MkmAPIException;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Kevin Zellweger
 * @Date 05.07.20
 */
@RestController
@RequestMapping("/stock")
public class StockController {

  @Autowired
  private ArticleService articleService;

  @Autowired
  private StockService stockService;

  @Autowired
  private ArticleMapper articleMapper;

  @Autowired
  private ExpansionServie expansionService;

  /**
   * Add new Articles to Stock. This Operation will first post the new Articles to MKM. The Result
   * of MKM will be persisted in the local database.
   * <p>
   * TODO: change implementation to fulfill what comment implies
   *
   * @param articleDtos new Articles to post to MKM
   * @return List of the new added Articles
   * @throws IOException
   */
  @PostMapping
  public ResponseEntity<List<ArticleDto>> addArticlesToStock(
      @RequestBody List<ArticleDto> articleDtos)
      throws IOException {
    return new ResponseEntity<>(stockService.postNewArticlesToStock(articleDtos), HttpStatus.OK);
  }

  //TODO return DTO and Move to designated controller
  @GetMapping("/expansion/{name}")
  public List<ExpansionEntity> findExpansionsByName(@PathVariable("name") String name)
      throws IOException {
    return expansionService.findAllByNameContaining(name);
  }

  @GetMapping
  public List<ArticleDto> loadMyStock() {
    //TODO: we have redundant entries in the database.... but why?
    List<ArticleEntity> articles = articleService.findAll();
    return articles.stream().map(articleMapper::entityToDto).collect(Collectors.toList());
  }

  @GetMapping("/sync")
  public List<ArticleDto> syncStockWithMKM() {
    return articleService.reloadStockFromMkm();
  }

  @PutMapping
  public ResponseEntity<List<ArticleDto>> updateArticlesInStock(
      @RequestBody List<ArticleDto> articleDtos)
      throws MkmAPIException {
    return new ResponseEntity<>(stockService.updateArticlesInStock(articleDtos), HttpStatus.OK);
  }
}
