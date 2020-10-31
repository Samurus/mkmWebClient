package de.cardmarket4j;

import com.google.gson.JsonElement;
import de.cardmarket4j.entity.enumeration.HTTPMethod;
import org.javatuples.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public abstract class AbstractService {
	protected final Logger LOGGER = LoggerFactory.getLogger(getClass().getSimpleName());
	final CardMarketService cardMarket;

	public AbstractService(CardMarketService cardMarket) {
		this.cardMarket = cardMarket;
	}

	protected Pair<Integer, JsonElement> getLastResponse() {
		return cardMarket.getLastResponse();
	}

    protected JsonElement legacyRequest(String URL, HTTPMethod httpMethod) throws IOException {
        return cardMarket.legacyRequest(URL, httpMethod).getValue1();
    }

	protected JsonElement request(String URL, HTTPMethod httpMethod) throws IOException {
		return cardMarket.request(URL, httpMethod).getValue1();
	}

	protected JsonElement request(String URL, HTTPMethod httpMethod, String requestBody) throws IOException {
		return cardMarket.request(URL, httpMethod, requestBody).getValue1();
	}
}
