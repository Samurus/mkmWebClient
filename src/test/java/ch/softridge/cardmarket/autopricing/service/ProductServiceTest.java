package ch.softridge.cardmarket.autopricing.service;

import ch.softridge.cardmarket.autopricing.domain.service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author Kevin Zellweger
 * @Date 28.10.20
 */
@SpringBootTest
@EnableConfigurationProperties
public class ProductServiceTest {

    @Autowired
    ProductService service;

    @Test
    public void loadMkmProducts(){
        service.loadMkmProductlist();
    }

}
