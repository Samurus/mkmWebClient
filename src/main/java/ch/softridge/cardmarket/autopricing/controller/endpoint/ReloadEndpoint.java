package ch.softridge.cardmarket.autopricing.controller.endpoint;

import ch.softridge.cardmarket.autopricing.controller.model.ArticleDto;
import ch.softridge.cardmarket.autopricing.repository.model.ArticleEntity;
import ch.softridge.cardmarket.autopricing.repository.model.ArticlePrice;
import ch.softridge.cardmarket.autopricing.repository.model.ExpansionEntity;
import ch.softridge.cardmarket.autopricing.repository.model.ProductEntity;
import ch.softridge.cardmarket.autopricing.service.ArticleService;
import ch.softridge.cardmarket.autopricing.service.PriceService;
import ch.softridge.cardmarket.autopricing.service.ProductService;
import ch.softridge.cardmarket.autopricing.service.mapper.ArticleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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


    @GetMapping("/prices/{name}")
    public List<ArticlePrice> reloadPricesRecommendations(@PathVariable("name") String name) throws IOException {
        return priceService.reloadPricesRecommendations(name);
    }

    @GetMapping("/stock")
    public List<ArticleDto> reloadMyStockFromMkm(Model model){
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
        return productService.persistProductFile();
    }



    @GetMapping("/expansions")
    public List<ExpansionEntity> reloadExpansions() throws IOException {
        return productService.persistExpansions();
    }
}