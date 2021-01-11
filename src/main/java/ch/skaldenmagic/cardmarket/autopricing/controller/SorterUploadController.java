package ch.skaldenmagic.cardmarket.autopricing.controller;

import ch.skaldenmagic.cardmarket.autopricing.domain.service.CardService;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * @author Kevin Zellweger
 * @Date 05.07.20
 */
@Controller
public class SorterUploadController {

  private static final Logger log = LoggerFactory.getLogger(SorterUploadController.class);
  private final String UPLOAD_DIR = "./uploads/";
  private final CardService cardService;

  @Autowired
  public SorterUploadController(CardService cardService) {
    this.cardService = cardService;
  }

  @GetMapping("/sorterUpload")
  public String main() {
    return "sorterUpload";
  }

  @PostMapping("/upload")
  public ResponseEntity uploadFile(@RequestParam("file") MultipartFile file,
      RedirectAttributes attributes) {

    // check if file is empty
    if (file.isEmpty()) {
      return ResponseEntity.badRequest().build();
    }
    try {
      cardService.addAll(cardService.readSorterCSV(file.getBytes()));
    } catch (IOException ioException) {
      ioException.printStackTrace();
    }
    return ResponseEntity.ok().build();
  }
}
