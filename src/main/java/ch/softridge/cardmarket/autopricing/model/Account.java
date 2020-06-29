package ch.softridge.cardmarket.autopricing.model;

import org.springframework.beans.factory.annotation.Value;

/**
 * @author Kevin Zellweger
 * @Date 29.06.20
 */
public class Account {
    @Value("${mkm.appToken}")
    private String appToken;
    @Value("${mkm.appSecret}")
    private String appSecret;
    @Value("${mkm.accessToken}")
    private String accessToken;
    @Value("${mkm.accessTokenSecret}")
    private String accessTokenSecret;




}
