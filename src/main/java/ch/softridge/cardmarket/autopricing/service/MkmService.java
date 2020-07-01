package ch.softridge.cardmarket.autopricing.service;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Kevin Zellweger
 * @Date 01.07.20
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

    private HttpEntity entity;
    private RestTemplate restTemplate = new RestTemplate();
    private HttpHeaders headers = new HttpHeaders();
    private static Logger log = LoggerFactory.getLogger(MkmService.class);

    protected MkmService(){};

    public <T> T mkmRequest(Class<T> type,String route) {
        String url = _apiUrl + route;
        //request(mkmRequest);
        headers.set("Authorization",encryptHttpHeader(url));
        entity = new HttpEntity(headers);
        ResponseEntity<T> res = restTemplate.exchange(url, HttpMethod.GET,entity,type);
        log.info(res.getStatusCode().toString());
        log.info(res.getBody().toString());
        return res.getBody();
    }

    public <T> T mkmRequest(Class<T> type,String route,Map<String,String> params) {
        String url = _apiUrl + route;
        //request(mkmRequest);
        headers.set("Authorization",encryptHttpHeader(url));
        entity = new HttpEntity(headers);
        ResponseEntity<T> res = restTemplate.exchange(url, HttpMethod.GET,entity,type,params);
        log.info(res.getStatusCode().toString());
        log.info(res.getBody().toString());
        return res.getBody();
    }

    public <T> T mkmRequest(Class<T> type,String route,String params) {
        String url = _apiUrl + route + params;
        //request(mkmRequest);
        headers.set("Authorization",encryptHttpHeader(url));
        entity = new HttpEntity(headers);
        ResponseEntity<T> res = restTemplate.exchange(url, HttpMethod.GET,entity,type);
        log.info(res.getStatusCode().toString());
        log.info(res.getBody().toString());
        return res.getBody();
    }

    private String encryptHttpHeader(String url) {
        log.info("URL: ",url);
        Map<String, String> oAuthPorps = new HashMap<>();
        String realm = url;
        String oauth_version = "1.0";
        String oauth_consumer_key = _mkmAppToken;
        String oauth_token = _mkmAccessToken;
        String oauth_signature_method = "HMAC-SHA1";
        String oauth_timestamp = "" + (System.currentTimeMillis() / 1000);
        String oauth_nonce = "" + System.currentTimeMillis();
        String authorizationProperty = null;
        try {

            String encodedRequestURL = rawurlencode(url);
            String baseString = "GET&" + encodedRequestURL + "&";

            String paramString = "oauth_consumer_key=" + rawurlencode(oauth_consumer_key) + "&" +
                    "oauth_nonce=" + rawurlencode(oauth_nonce) + "&" +
                    "oauth_signature_method=" + rawurlencode(oauth_signature_method) + "&" +
                    "oauth_timestamp=" + rawurlencode(oauth_timestamp) + "&" +
                    "oauth_token=" + rawurlencode(oauth_token) + "&" +
                    "oauth_version=" + rawurlencode(oauth_version);

            baseString = baseString + rawurlencode(paramString);

            String signingKey = rawurlencode(_mkmAppSecret) +
                    "&" +
                    rawurlencode(_mkmAccessTokenSecret);

            Mac mac = Mac.getInstance("HmacSHA1");
            SecretKeySpec secret = new SecretKeySpec(signingKey.getBytes(), mac.getAlgorithm());
            mac.init(secret);
            byte[] digest = mac.doFinal(baseString.getBytes());
            String oauth_signature = DatatypeConverter.printBase64Binary(digest);    //Base64.encode(digest) ;

            authorizationProperty =
                    "OAuth " +
                            "realm=\"" + realm + "\", " +
                            "oauth_version=\"" + oauth_version + "\", " +
                            "oauth_timestamp=\"" + oauth_timestamp + "\", " +
                            "oauth_nonce=\"" + oauth_nonce + "\", " +
                            "oauth_consumer_key=\"" + oauth_consumer_key + "\", " +
                            "oauth_token=\"" + oauth_token + "\", " +
                            "oauth_signature_method=\"" + oauth_signature_method + "\", " +
                            "oauth_signature=\"" + oauth_signature + "\"";
            oAuthPorps.put("realm", realm);
            oAuthPorps.put("oauth_version", oauth_version);
            oAuthPorps.put("oauth_timestamp", oauth_timestamp);
            oAuthPorps.put("oauth_nonce", oauth_nonce);
            oAuthPorps.put("oauth_consumer_key", oauth_consumer_key);
            oAuthPorps.put("oauth_token", oauth_token);
            oAuthPorps.put("oauth_signature_method", oauth_signature_method);
            oAuthPorps.put("oauth_signature", oauth_signature);
        } catch (Exception e) {

            log.error("(!) Error while requesting "+url);
        }
        return authorizationProperty;
    }

    /**
     * Encoding function. To avoid deprecated version, the encoding used is UTF-8.
     *
     * @param str
     * @return given param as UTF-8 String
     * @throws UnsupportedEncodingException
     */
    private String rawurlencode(String str) throws UnsupportedEncodingException {
        return URLEncoder.encode(str, "UTF-8");
    }

    public <Card> List<Card> loadObjectList(Class<Card> type, String fileName) {
        try {
            CsvSchema bootstrapSchema = CsvSchema.emptySchema().withHeader();
            CsvMapper mapper = new CsvMapper();
            File file = new ClassPathResource(fileName).getFile();
            MappingIterator<Card> readValues =
                    mapper.reader(type).with(bootstrapSchema).readValues(file);
            return readValues.readAll();
        } catch (Exception e) {
            //logger.error("Error occurred while loading object list from file " + fileName, e);
            return Collections.emptyList();
        }
    }

}

