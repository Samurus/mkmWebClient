package ch.softridge.cardmarket.autopricing.service;

import de.cardmarket4j.CardMarketService;
import de.cardmarket4j.entity.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * @author Kevin Zellweger
 * @Date 01.07.20
 *
 * Helper Service responsible to build the Requests to the MKM API
 */
@Service
public class MkmService {
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

    private final CardMarketService cardMarketService;

    @Autowired
    public MkmService() {
        cardMarketService = new CardMarketService(_mkmAppToken,_mkmAppSecret,_mkmAccessToken,_mkmAccessTokenSecret);
        cardMarketService.setSandBoxMode(_sandboxMode);
    }

    public Account getAccount() throws IOException {
        return cardMarketService.getAccountService().getAccount();
    }

}

