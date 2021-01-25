package ch.skaldenmagic.cardmarket.autopricing.domain.service;

import de.cardmarket4j.CardMarketService;
import de.cardmarket4j.entity.Account;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @author Kevin Zellweger
 * @Date 01.07.20
 * <p>
 * Helper Service responsible to build the Requests to the MKM API
 */
@Service
public class MkmService {

  private static final Logger LOGGER = LoggerFactory.getLogger(MkmService.class);
  public CardMarketService cardMarketService;
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

  public Account getAccount() throws IOException {
    return cardMarketService.getAccountService().getAccount();
  }

  public CardMarketService getCardMarket() {
    if (null == cardMarketService) {
      cardMarketService = new CardMarketService(_mkmAppToken, _mkmAppSecret, _mkmAccessToken,
          _mkmAccessTokenSecret);
      cardMarketService.setSandBoxMode(_sandboxMode);
    }
    LOGGER.info("{} of {} requests used today.", cardMarketService.getRequestCount(),
        cardMarketService.getRequestLimit());
    return cardMarketService;
  }


}

