package ch.skaldenmagic.cardmarket.autopricing.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author Kevin Zellweger
 * @Date 29.06.20
 * <p>
 * Configuration for custom Properties. Used in: - application-dev.yml - application-prod.yml -
 * application-test.yml
 * <p>
 * Those configuration contain the credentials to communicatie with the MKM-API. Therefore there are
 * not stored on Github.
 * <p>
 * Create your owen files in this format: mkm: appToken: "Atl7XXXXXXXX" appSecret:
 * "YdQXXXXXXXXXXXXXXXXXXXXX" accessToken: "CMXXXXXXXXXXXXXXXXXXXXXXXXX" accessTokenSecret:
 * "gLXXXXXXXXXXXXXXXXXXXXXXXXXXXXX" apiUrl: "https://sandbox.cardmarket.com/ws/v2.0/output.json/"
 */

@Configuration
@ConfigurationProperties(prefix = "mkm")
public class Config {

  private String appToken;
  private String appSecret;
  private String accessToken;
  private String accessTokenSecret;
  private String apiUrl;
  private boolean sandboxMode;

  public String getAccessToken() {
    return accessToken;
  }

  public void setAccessToken(String accessToken) {
    this.accessToken = accessToken;
  }

  public String getAccessTokenSecret() {
    return accessTokenSecret;
  }

  public void setAccessTokenSecret(String accessTokenSecret) {
    this.accessTokenSecret = accessTokenSecret;
  }

  public String getApiUrl() {
    return apiUrl;
  }

  public void setApiUrl(String apiUrl) {
    this.apiUrl = apiUrl;
  }

  public String getAppSecret() {
    return appSecret;
  }

  public void setAppSecret(String appSecret) {
    this.appSecret = appSecret;
  }

  public String getAppToken() {
    return appToken;
  }

  public void setAppToken(String appToken) {
    this.appToken = appToken;
  }

  public boolean isSandboxMode() {
    return sandboxMode;
  }

  public void setSandboxMode(boolean sandboxMode) {
    this.sandboxMode = sandboxMode;
  }

}
