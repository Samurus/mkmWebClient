package ch.skaldenmagic.cardmarket.autopricing.cardmarketservice;

import static org.springframework.test.util.AssertionErrors.assertTrue;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.junit.jupiter.api.Test;

/**
 * @author Kevin Zellweger
 * @Date 11.03.21
 */
public class JsonTest {

  @Test
  public void JSonformatTest() {
    String responseString = "Some shit";
    JsonElement errorResponse = JsonParser
        .parseString("{'error':true,'message':'" + responseString + "'}");

    assertTrue("IsError", errorResponse.getAsJsonObject().get("error").getAsBoolean());
  }

}
