package ch.softridge.cardmarket.autopricing.controller;


import ch.softridge.cardmarket.autopricing.model.Card;
import ch.softridge.cardmarket.autopricing.model.Rarity;
import ch.softridge.cardmarket.autopricing.service.CardService;
import ch.softridge.cardmarket.autopricing.service.MkmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * @author Kevin Zellweger
 * @Date 29.06.20
 *
 * Controller which routes the interaction with the MKM API
 *
 */
@Controller
public class ApiController {
    private MkmService mkmService;
    private CardService cardService;

    @Autowired
    public ApiController(MkmService mkmService, CardService cardService){
        this.mkmService = mkmService;
        this.cardService = cardService;
    }

    @GetMapping({ "/", "/index" })
    public String index(Model model)
    {
        model.addAttribute("card",new Card("None","None", Rarity.COMMON,1,0.2,0.5));
        //model.addAttribute("name","John");
        return "index";
    }

    @PostMapping("/some")
    public String save(Card card, Model model) {
        model.addAttribute("card",card);
        return "saved";
    }

}
