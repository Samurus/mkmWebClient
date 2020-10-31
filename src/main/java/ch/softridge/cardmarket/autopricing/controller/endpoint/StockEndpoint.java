package ch.softridge.cardmarket.autopricing.controller.endpoint;

import ch.softridge.cardmarket.autopricing.controller.model.ArticleDto;
import ch.softridge.cardmarket.autopricing.repository.model.ArticleEntity;
import ch.softridge.cardmarket.autopricing.repository.model.ExpansionEntity;
import ch.softridge.cardmarket.autopricing.service.ArticleService;
import ch.softridge.cardmarket.autopricing.service.PriceService;
import ch.softridge.cardmarket.autopricing.service.ProductService;
import ch.softridge.cardmarket.autopricing.service.mapper.ArticleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

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
    private ProductService productService;

    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private PriceService priceService;

    @GetMapping("/articles")
    public List<ArticleDto> loadMyStock(){
        List<ArticleEntity> articles = articleService.findAll();
        return articles.stream().map(articleMapper::articleEntityToDto).collect(Collectors.toList());
    }

    @PostMapping("/articles")
    public List<ArticleEntity> updateMyStock(@RequestBody List<ArticleDto> articleDtos) throws IOException {
        return articleService.updateAll(articleDtos);
    }

    @GetMapping("/articles/min-price")
    public List<ArticleDto> loadStockWithMinPrice(){
        return articleService.findAllWithMinPrice();
    }

    @GetMapping("/expansion/{name}")
    public List<ExpansionEntity> findExpansionsByName(@PathVariable("name") String name) throws IOException {
        return productService.findExpansionsByName(name);
    }

    @GetMapping("/articles/expansion/{id}")
    public List<ArticleDto> findAllArticlesWithCheapestPriceByExpansion(@PathVariable("id") Integer id) throws IOException {
        return productService.findAllArticlesWithCheapestPriceByExpansion(id);
    }

}
