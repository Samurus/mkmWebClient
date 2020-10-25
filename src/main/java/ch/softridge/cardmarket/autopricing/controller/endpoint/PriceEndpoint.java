package ch.softridge.cardmarket.autopricing.controller.endpoint;

import ch.softridge.cardmarket.autopricing.controller.model.ArticleDto;
import ch.softridge.cardmarket.autopricing.repository.ArticleRepository;
import ch.softridge.cardmarket.autopricing.repository.model.ArticleEntity;
import ch.softridge.cardmarket.autopricing.repository.model.ArticlePrice;
import ch.softridge.cardmarket.autopricing.service.ArticleService;
import ch.softridge.cardmarket.autopricing.service.PriceService;
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

/**
 * @author Kevin Zellweger
 * @Date 05.07.20
 */
@RestController
@RequestMapping("/price")
public class PriceEndpoint {

    @Autowired
    private PriceService priceService;

    @GetMapping("/{name}")
    public List<ArticlePrice> findPriceRecommendations(@PathVariable("name") String name){
        return priceService.findAll();
    }

    @GetMapping("/{name}/reload")
    public List<ArticlePrice> reloadPricesRecommendations(@PathVariable("name") String name) throws IOException {
        return priceService.reloadPricesRecommendations(name);
    }
}
