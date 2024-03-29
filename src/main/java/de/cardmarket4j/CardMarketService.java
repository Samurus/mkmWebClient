package de.cardmarket4j;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import de.cardmarket4j.entity.enumeration.HTTPMethod;
import de.cardmarket4j.service.*;
import de.cardmarket4j.util.HTTPUtils;
import org.javatuples.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class CardMarketService {
	private static final String URI = "https://api.cardmarket.com/ws/v2.0/output.json/";
	private static final String URI_LEGACY = "https://api.cardmarket.com/ws/v2.0/";
	private static final String URI_SANDBOX = "https://sandbox.cardmarket.com/ws/v2.0/output.json/";
	private static final String URI_SANDBOX_LEGACY = "https://sandbox.cardmarket.com/ws/v2.0/";
	private static Logger LOGGER = LoggerFactory.getLogger("CardMarketService");
	private boolean sandBoxMode;
	private final AuthenticationService authenticationService;
	private final AccountService accountService;
	private final MarketplaceService marketplaceService;
	private final StockService stockService;
	private final OrderService orderService;
	private Pair<Integer, JsonElement> lastResponse;
	private int requestCount;
	private int requestLimit;

	public CardMarketService(String appToken, String appSecret, String accessToken, String accessTokenSecret) {
		this.authenticationService = new AuthenticationService(appToken, appSecret, accessToken, accessTokenSecret);
		this.accountService = new AccountService(this);
		this.marketplaceService = new MarketplaceService(this);
		this.stockService = new StockService(this);
		this.orderService = new OrderService(this);
	}

	public AccountService getAccountService() {
		return accountService;
	}

	public Pair<Integer, JsonElement> getLastResponse() {
		return lastResponse;
	}

	public MarketplaceService getMarketplaceService() {
		return marketplaceService;
	}

	public OrderService getOrderService() {
		return orderService;
	}

	public int getRequestCount() {
		return requestCount;
	}

	public int getRequestLimit() {
		return requestLimit;
	}

	public StockService getStockService() {
		return stockService;
	}

	public boolean isSandBoxMode() {
		return sandBoxMode;
	}


    Pair<Integer, JsonElement> legacyRequest(String URL, HTTPMethod httpMethod) throws IOException {
	    try {
            String uri = (sandBoxMode ? URI_SANDBOX_LEGACY : URI_LEGACY) + URL.replaceAll("\\s", "%20");
            HttpURLConnection connection = (HttpURLConnection) new URL(uri).openConnection();

            String oAuthSignature = authenticationService.createOAuthSignature(uri, httpMethod);
            connection.addRequestProperty("Authorization", oAuthSignature);
            connection.setRequestMethod(httpMethod.toString());

            LOGGER.debug("Request:\t{} {}", httpMethod.toString(), uri);
            LOGGER.trace("Authorization:\t{}", oAuthSignature);
            connection.connect();

            int responseCode = connection.getResponseCode();
            LOGGER.trace("Response Code:\t{}", responseCode);
            LOGGER.trace("Response Header: {}", HTTPUtils.getResponseHeader(connection));
            requestCount = connection.getHeaderFieldInt("X-Request-Limit-Count", -1);
            requestLimit = connection.getHeaderFieldInt("X-Request-Limit-Max", -1);

            BufferedReader rd = new BufferedReader(new InputStreamReader(
                    (responseCode == 200 || responseCode == 206 || responseCode == 204) ? connection.getInputStream()
                            : connection.getErrorStream()));
            StringBuffer sb = new StringBuffer();
            String line;
            while ((line = rd.readLine()) != null) {
                sb.append(line);
            }
            rd.close();

            String responseString = sb.toString();
            LOGGER.trace("Response Body:\t{}", responseString);
            JsonElement jResponse = getJsonElementFromXML(responseString);
            LOGGER.debug("Response:\t{} {}", responseCode, jResponse);
            lastResponse = new Pair<>(responseCode, jResponse);
            return lastResponse;
        } catch (MalformedURLException | InvalidKeyException | NoSuchAlgorithmException | NullPointerException e) {
            LOGGER.error("An critical error occured", e);
            System.exit(-1);
            throw new IOException(e);
        }
    }

    private JsonElement getJsonElementFromXML(String responseString) throws IOException {
        XmlMapper xmlMapper = new XmlMapper();
        JsonNode node = xmlMapper.readTree(responseString.getBytes());
        JsonElement element = JsonParser.parseString(node.toString());
        return element;
    }

    Pair<Integer, JsonElement> request(String URL, HTTPMethod httpMethod) throws IOException {
		try {
			String uri = (sandBoxMode ? URI_SANDBOX : URI) + URL.replaceAll("\\s", "%20");
			HttpURLConnection connection = (HttpURLConnection) new URL(uri).openConnection();

			String oAuthSignature = authenticationService.createOAuthSignature(uri, httpMethod);
			connection.addRequestProperty("Authorization", oAuthSignature);
			connection.setRequestMethod(httpMethod.toString());

			LOGGER.debug("Request:\t{} {}", httpMethod.toString(), uri);
			LOGGER.trace("Authorization:\t{}", oAuthSignature);
			connection.connect();

			int responseCode = connection.getResponseCode();
			LOGGER.trace("Response Code:\t{}", responseCode);
			LOGGER.trace("Response Header: {}", HTTPUtils.getResponseHeader(connection));
			requestCount = connection.getHeaderFieldInt("X-Request-Limit-Count", -1);
			requestLimit = connection.getHeaderFieldInt("X-Request-Limit-Max", -1);

			BufferedReader rd = new BufferedReader(new InputStreamReader(
					(responseCode == 200 || responseCode == 206 || responseCode == 204) ? connection.getInputStream()
							: connection.getErrorStream()));
			StringBuffer sb = new StringBuffer();
			String line;
			while ((line = rd.readLine()) != null) {
				sb.append(line);
			}
			rd.close();

			String responseString = sb.toString();
			LOGGER.trace("Response Body:\t{}", responseString);
			JsonElement jResponse = JsonParser.parseString(responseString);
			LOGGER.debug("Response:\t{} {}", responseCode, jResponse);
			lastResponse = new Pair<>(responseCode, jResponse);
			return lastResponse;
		} catch (MalformedURLException | InvalidKeyException | NoSuchAlgorithmException | NullPointerException e) {
			LOGGER.error("An critical error occured", e);
			System.exit(-1);
			return null;
		}
	}

	Pair<Integer, JsonElement> request(String URL, HTTPMethod httpMethod, String requestBody) throws IOException {
		try {
			String uri = (sandBoxMode ? URI_SANDBOX : URI) + URL.replaceAll("\\s", "%20");
			HttpURLConnection connection = (HttpURLConnection) new URL(uri).openConnection();

			String oAuthSignature = authenticationService.createOAuthSignature(uri, httpMethod);
			connection.addRequestProperty("Authorization", oAuthSignature);
			connection.setDoOutput(true);
			connection.setRequestMethod(httpMethod.toString());

			LOGGER.debug("Request:\t{} {}", httpMethod.toString(), uri);
			LOGGER.trace("Authorization:\t{}", oAuthSignature);
			connection.connect();

			OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream());
			out.write(requestBody);
			out.close();
			LOGGER.trace("Request Body:\t{}", requestBody);

			int responseCode = connection.getResponseCode();
			LOGGER.trace("Response Code:\t{}", responseCode);
			LOGGER.trace("Response Header: {}", HTTPUtils.getResponseHeader(connection));
			requestCount = connection.getHeaderFieldInt("X-Request-Limit-Count", -1);
			requestLimit = connection.getHeaderFieldInt("X-Request-Limit-Max", -1);

			BufferedReader rd = new BufferedReader(new InputStreamReader(
					(responseCode == 200 || responseCode == 206 || responseCode == 204) ? connection.getInputStream()
							: connection.getErrorStream()));
			StringBuffer sb = new StringBuffer();
			String line;
			while ((line = rd.readLine()) != null) {
				sb.append(line);
			}
			rd.close();

			String responseString = sb.toString();
			LOGGER.trace("Response Body:\t{}", responseString);
			JsonElement jResponse = JsonParser.parseString(responseString);
			LOGGER.debug("Response:\t{} {}", responseCode, jResponse);
			return new Pair<>(responseCode, jResponse);
		} catch (MalformedURLException | InvalidKeyException | NoSuchAlgorithmException | NullPointerException e) {
			LOGGER.error("An critical error occured", e);
			System.exit(-1);
			return null;
		}
	}

	public void setSandBoxMode(boolean sandBoxMode) {
		this.sandBoxMode = sandBoxMode;
	}

}
