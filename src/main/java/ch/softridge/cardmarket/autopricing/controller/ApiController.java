package ch.softridge.cardmarket.autopricing.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * @author Kevin Zellweger
 * @Date 29.06.20
 */
@RestController
public class ApiController {
    @Value("${mkm.appToken}")
    String appToken;
    @Value("${server.port}")
    int port;
    @GetMapping("/")
    public String index(){
        return appToken + " at: " + port;
    }

    private String buildMkmRequest (String requestUrl){
        //TODO: autogen
        return null;
    }

    /**
     * Encoding function. To avoid deprecated version, the encoding used is UTF-8.
     * @param str
     * @return given param as UTF-8 String
     * @throws UnsupportedEncodingException
     */
    private String rawurlencode(String str) throws UnsupportedEncodingException {
        return URLEncoder.encode(str, "UTF-8");
    }

}
