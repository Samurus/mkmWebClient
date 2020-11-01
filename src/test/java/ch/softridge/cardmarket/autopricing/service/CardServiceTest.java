package ch.softridge.cardmarket.autopricing.service;

import ch.softridge.cardmarket.autopricing.domain.service.CardService;
import ch.softridge.cardmarket.autopricing.domain.model.Card;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

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
