package ch.skaldenmagic.cardmarket.autopricing.service;

import ch.skaldenmagic.cardmarket.autopricing.domain.service.UploadService;
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
  UploadService uploadService;

    @Test
    public void testReadCsv(){
    }

}
