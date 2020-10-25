package ch.softridge.cardmarket.autopricing.controller.endpoint;

import ch.softridge.cardmarket.autopricing.controller.model.ArticleDto;
import ch.softridge.cardmarket.autopricing.repository.model.ArticleEntity;
import ch.softridge.cardmarket.autopricing.service.ArticleService;
import ch.softridge.cardmarket.autopricing.service.mapper.ArticleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
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
@RequestMapping("/article")
public class ArticleEndpoint {

    @Autowired
    private ArticleService articleService;

    @Autowired
    private ArticleMapper articleMapper;

    @GetMapping("/stock/reload")
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

    @GetMapping("/stock")
    public List<ArticleDto> loadMyStock(Model model){
            List<ArticleEntity> articles = articleService.findAll();
            return articles.stream().map(articleMapper::articleEntityToDto).collect(Collectors.toList());
    }
}
