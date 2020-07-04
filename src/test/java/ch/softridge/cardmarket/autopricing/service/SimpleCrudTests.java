package ch.softridge.cardmarket.autopricing.service;

import ch.softridge.cardmarket.autopricing.model.Card;
import ch.softridge.cardmarket.autopricing.model.MkmCard;
import ch.softridge.cardmarket.autopricing.model.Rarity;
import ch.softridge.cardmarket.autopricing.model.ScryfallCard;
import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;

/**
 * @author Kevin Zellweger
 * @Date 04.07.20
 */
@SpringBootTest()
@EnableConfigurationProperties
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SimpleCrudTests {

    private static final Logger log = LoggerFactory.getLogger(MkmServiceTest.class);
    @Autowired
    private CardService cardService;
    @Autowired
    private MkmCardService mkmCardService;
    @Autowired
    private ScryfallCardService scryfallCardService;

    @BeforeEach
    public void clearDB()
    {
        cardService.deleteAll();
        mkmCardService.deleteAll();
        scryfallCardService.deleteAll();
    }

    @Test
    public void createCard(){
        Card card = new Card("title", "set", Rarity.COMMON, 1, 0.2, 0.4);
        cardService.addCard(card);
        Assertions.assertEquals(1, cardService.findAll().size());
        mkmCardService.addCard(new MkmCard(1234,5678,3,"Some",card));
        Assertions.assertEquals(1,mkmCardService.findAll().size());
        scryfallCardService.addCard(new ScryfallCard("asdasdf",card));
        Assertions.assertEquals(1,scryfallCardService.findAll().size());
    }
    @Test
    public void addSameCard(){
        cardService.addCard(new Card("title", "set", Rarity.COMMON, 1, 0.2, 0.4));
        Assertions.assertThrows(DataIntegrityViolationException.class, () ->{
            cardService.addCard(new Card("title", "set", Rarity.COMMON, 1, 0.2, 0.4));
        });
    }

}
