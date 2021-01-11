package ch.skaldenmagic.cardmarket.autopricing.controller;

import ch.skaldenmagic.cardmarket.autopricing.domain.service.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author Kevin Zellweger
 * @Date 05.07.20
 */
@Controller
public class CardController {

  CardService cardService;

  @Autowired
  public CardController(CardService cardService) {
    this.cardService = cardService;
  }

  @GetMapping("/cards")
  public String getCards(Model model) {
    model.addAttribute("cards", cardService.findAll());
    return "cards";
  }
}
