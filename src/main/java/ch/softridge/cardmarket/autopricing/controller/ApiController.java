package ch.softridge.cardmarket.autopricing.controller;

import ch.softridge.cardmarket.autopricing.model.Card;
import ch.softridge.cardmarket.autopricing.model.MkmAccount;
import ch.softridge.cardmarket.autopricing.model.Rarity;
import ch.softridge.cardmarket.autopricing.service.CardService;
import ch.softridge.cardmarket.autopricing.service.MkmService;
import com.fasterxml.jackson.core.FormatSchema;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Kevin Zellweger
 * @Date 29.06.20
 */
@RestController
public class ApiController {
    private Throwable _lastError;
    private int _lastCode;
    private String _lastContent;
    private Logger logger = LoggerFactory.getLogger(ApiController.class);

    @Autowired
    private MkmService mkmService;
    @Autowired
    private CardService cardService;

    @GetMapping("/")
    public String index(){
        return "Index";
    }

    @GetMapping("/mkmRequest")
    public void account() {
    }

}
