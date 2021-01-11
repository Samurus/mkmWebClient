package ch.skaldenmagic.cardmarket.autopricing.service;

import ch.skaldenmagic.cardmarket.autopricing.domain.model.Card;
import ch.skaldenmagic.cardmarket.autopricing.domain.service.CardService;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author Kevin Zellweger
 * @Date 05.07.20
 */
@SpringBootTest
@EnableConfigurationProperties
public class CardServiceTest {

    @Autowired
    CardService cardService;

    @Test
    public void testReadCsv(){
        List<Card> result = cardService.readSorterCSV("src/test/resources/results.csv");
        Assertions.assertEquals(68,result.size());
    }

}
