package ch.softridge.cardmarket.autopricing.service;

import ch.softridge.cardmarket.autopricing.config.Config;
import ch.softridge.cardmarket.autopricing.model.MkmAccount;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.validation.beanvalidation.SpringValidatorAdapter;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Kevin Zellweger
 * @Date 01.07.20
 */
@SpringBootTest(classes = {MkmService.class, Config.class})
@EnableConfigurationProperties
public class MkmServiceTest {
    private static Logger log = LoggerFactory.getLogger(MkmServiceTest.class);
    @Autowired
    private MkmService service;

    @Value("${mkm.appToken}")
    private String _mkmAppToken;

    @Test
    public void testMkmAccount(){
        MkmAccount res;
        res = service.mkmRequest(MkmAccount.class,"account");
        log.info(res.getAccount().keySet().toString());
        Assertions.assertEquals("TestUser891057",res.getAccount().get("username"));
        Assertions.assertEquals("Atl7VzTyADarIJBG",_mkmAppToken);
    }

    @Test
    public void testQueryAsMap(){
        String res;
        Map<String,String> queryParams = new HashMap<>();
        queryParams.put("search","Springleaf");
        queryParams.put("idGame","1");
        queryParams.put("idLanguage","1");
        res = service.mkmRequest(String.class,"products/find",queryParams);
    }

    @Test
    public void testQueryAsString(){
        String res;
        String queryParams = "?search=Springleaf&idGame=1&idLanguage=1";
        res = service.mkmRequest(String.class,"products/find",queryParams);
    }
}
