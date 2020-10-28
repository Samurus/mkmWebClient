package ch.softridge.cardmarket.autopricing.service;

import ch.softridge.cardmarket.autopricing.config.Config;
import ch.softridge.cardmarket.autopricing.domain.service.MkmService;
import com.neovisionaries.i18n.LanguageCode;
import de.cardmarket4j.CardMarketService;
import de.cardmarket4j.entity.Account;
import de.cardmarket4j.entity.Article;
import de.cardmarket4j.entity.Product;
import de.cardmarket4j.entity.enumeration.Condition;
import de.cardmarket4j.entity.enumeration.Game;
import de.cardmarket4j.entity.util.ProductFilter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * @author Kevin Zellweger
 * @Date 01.07.20
 */
@SpringBootTest(classes = {MkmService.class, Config.class})
@EnableConfigurationProperties
public class MkmServiceTest {
    private static final Logger log = LoggerFactory.getLogger(MkmServiceTest.class);
    CardMarketService cardMarketService;
    @Value("${mkm.appToken}")
    private String _mkmAppToken;
    @Value("${mkm.appSecret}")
    private String _mkmAppSecret;
    @Value("${mkm.accessToken}")
    private String _mkmAccessToken;
    @Value("${mkm.accessTokenSecret}")
    private String _mkmAccessTokenSecret;
    @Value("${mkm.apiUrl}")
    private String _apiUrl;
    @Value("${mkm.sandbox-mode}")
    private boolean _sandboxMode;

    @BeforeEach
    public void setUp() {
        cardMarketService = new CardMarketService(_mkmAppToken, _mkmAppSecret, _mkmAccessToken, _mkmAccessTokenSecret);
        cardMarketService.setSandBoxMode(_sandboxMode);
    }

    @Test
    public void testFindMkmArticleByName() {
        ProductFilter filter = new ProductFilter("Springleaf");
        filter.setGame(Game.MTG);
        filter.setMaxResults(10);
        try {
            Set<Product> products = cardMarketService.getMarketplaceService().getProduct(filter);
            assertEquals(2, products.size());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testMkmAccount() {
        try {
            Account res = cardMarketService.getAccountService().getAccount();
            assertEquals("TestUser891057", res.getUserName());
            assertEquals("Test", res.getFirstName());
            assertEquals("Account", res.getLastName());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testMkmStock() {
        ProductFilter filter = new ProductFilter("Springleaf");
        filter.setGame(Game.MTG);
        filter.setMaxResults(10);
        try {
            Set<Product> products = cardMarketService.getMarketplaceService().getProduct(filter);
            List<Article> articles = new ArrayList<>();
            for (Product p : products) {
                articles = cardMarketService.getStockService().insertArticle(new Article(p.getProductId(), LanguageCode.en,
                        "Test", BigDecimal.valueOf(0.5), 1, Condition.EXCELLENT, false, false, false));
                assertNotNull(articles);
                assertNotNull(articles.get(0).getArticleId());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
