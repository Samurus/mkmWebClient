package ch.softridge.cardmarket.autopricing.service;

import com.google.gson.JsonElement;
import de.cardmarket4j.CardMarketService;
import de.cardmarket4j.entity.Account;
import de.cardmarket4j.service.AccountService;
import de.cardmarket4j.service.MarketplaceService;
import de.cardmarket4j.service.OrderService;
import de.cardmarket4j.service.StockService;
import org.javatuples.Pair;
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

    public CardMarketService cardMarketService;

    public Account getAccount() throws IOException {
        return cardMarketService.getAccountService().getAccount();
    }

    public CardMarketService getCardMarket() {
        if (null == cardMarketService) {
            cardMarketService = new CardMarketService(_mkmAppToken, _mkmAppSecret, _mkmAccessToken, _mkmAccessTokenSecret);
            cardMarketService.setSandBoxMode(_sandboxMode);
        }
        return cardMarketService;
    }

}

