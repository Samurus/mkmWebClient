package ch.softridge.cardmarket.autopricing.domain.service;

import ch.softridge.cardmarket.autopricing.domain.entity.ArticlePriceEntity;
import ch.softridge.cardmarket.autopricing.domain.repository.PriceRepository;
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


    public List<ArticlePriceEntity> findAll() {
        return priceRepository.findAll();
    }

    public List<ArticlePriceEntity> reloadPricesRecommendations(String name) throws IOException {
        //TODO with user form accountinfo
        //String userName = mkmService.getAccount().getUserName();
        priceRepository.deleteAll();
        ResponseEntity<List<ArticlePriceEntity>> prices = new RestTemplate().exchange
                (pricerUri + name + "/min-price", HttpMethod.GET,
                        new HttpEntity<ArticlePriceEntity>(createHeaders(pricerUserName, pricerPassword)),
                        new ParameterizedTypeReference<List<ArticlePriceEntity>>() {});
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
