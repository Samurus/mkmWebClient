package ch.skaldenmagic.cardmarket.autopricing.controller;


import ch.skaldenmagic.cardmarket.autopricing.domain.model.Card;
import ch.skaldenmagic.cardmarket.autopricing.domain.model.Rarity;
import ch.skaldenmagic.cardmarket.autopricing.domain.service.CardService;
import ch.skaldenmagic.cardmarket.autopricing.domain.service.MkmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * @author Kevin Zellweger
 * @Date 29.06.20
 * <p>
 * Controller which routes the interaction with the MKM API
 */
@Controller
public class ApiController {

  private final MkmService mkmService;
  private final CardService cardService;

  @Autowired
  public ApiController(MkmService mkmService, CardService cardService) {
    this.mkmService = mkmService;
    this.cardService = cardService;
  }

  @GetMapping({"/", "/index"})
  public String index(Model model) {
    model.addAttribute("card", new Card("None", "None", Rarity.COMMON, 1, 0.2, 0.5));
    //model.addAttribute("name","John");
    return "index";
  }

  @PostMapping("/some")
  public String save(Card card, Model model) {
    model.addAttribute("card", card);
    return "saved";
  }

}
