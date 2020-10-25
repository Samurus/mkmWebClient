package ch.softridge.cardmarket.autopricing.service;

import ch.softridge.cardmarket.autopricing.repository.PriceRepository;
import ch.softridge.cardmarket.autopricing.repository.model.ArticlePrice;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.transaction.Transactional;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;

@Service
@Transactional
public class PriceService {

    @Autowired
    private PriceRepository priceRepository;

    @Autowired
    private MkmService mkmService;

    @Value("${pricer.uri}")
    private String pricerUri;

    @Value("${pricer.user}")
    private String pricerUserName;

    @Value("${pricer.password}")
    private String pricerPassword;


    public List<ArticlePrice> findAll() {
        return priceRepository.findAll();
    }

    public List<ArticlePrice> reloadPricesRecommendations(String name) throws IOException {
        //TODO with user form accountinfo
        //String userName = mkmService.getAccount().getUserName();
        priceRepository.deleteAll();
        ResponseEntity<List<ArticlePrice>> prices = new RestTemplate().exchange
                (pricerUri + name + "/min-price", HttpMethod.GET,
                        new HttpEntity<ArticlePrice>(createHeaders(pricerUserName, pricerPassword)),
                        new ParameterizedTypeReference<List<ArticlePrice>>() {});
        return priceRepository.saveAll(prices.getBody());
    }


    HttpHeaders createHeaders(String username, String password) {
        return new HttpHeaders() {{
            String auth = username + ":" + password;
            byte[] encodedAuth = Base64.encodeBase64(
                    auth.getBytes(Charset.forName("US-ASCII")));
            String authHeader = "Basic " + new String(encodedAuth);
            set("Authorization", authHeader);
        }};
    }

}
