package ch.softridge.cardmarket.autopricing.controller;


import ch.softridge.cardmarket.autopricing.service.CardService;
import ch.softridge.cardmarket.autopricing.service.MkmService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

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
