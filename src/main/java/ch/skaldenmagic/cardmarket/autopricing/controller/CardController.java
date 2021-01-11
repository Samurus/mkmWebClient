package ch.skaldenmagic.cardmarket.autopricing.controller;

import ch.skaldenmagic.cardmarket.autopricing.domain.service.UploadService;
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

  UploadService uploadService;

  @Autowired
  public CardController(UploadService uploadService) {
    this.uploadService = uploadService;
  }

  @GetMapping("/cards")
  public String getCards(Model model) {
    model.addAttribute("cards", uploadService.findAll());
    return "cards";
  }
}
