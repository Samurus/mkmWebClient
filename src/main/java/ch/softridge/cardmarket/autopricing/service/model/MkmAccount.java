package ch.softridge.cardmarket.autopricing.service.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.util.Map;

/**
 * @author Kevin Zellweger
 * @Date 29.06.20
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class MkmAccount implements Serializable {
    Map<String,Object> account;

    protected MkmAccount(){};

    public MkmAccount(Map<String, Object> account) {
        this.account = account;
    }

    public Map<String, Object> getAccount() {
        return account;
    }

    public void setAccount(Map<String, Object> account) {
        this.account = account;
    }
}
