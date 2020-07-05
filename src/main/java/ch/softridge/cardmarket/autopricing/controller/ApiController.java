package ch.softridge.cardmarket.autopricing.controller;


import ch.softridge.cardmarket.autopricing.service.CardService;
import ch.softridge.cardmarket.autopricing.service.MkmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Kevin Zellweger
 * @Date 29.06.20
 *
 * Controller which routes the interaction with the MKM API
 *
 */
@RestController
public class ApiController {
    private MkmService mkmService;
    private CardService cardService;

    @Autowired
    public ApiController(MkmService mkmService, CardService cardService){
        this.mkmService = mkmService;
        this.cardService = cardService;
    }

    @GetMapping("/")
    public String index(){
        return "Index";
    }

    @GetMapping("/mkmRequest")
    public void account() {
    }

}
