package ch.skaldenmagic.cardmarket.autopricing.controller;

import ch.skaldenmagic.cardmarket.autopricing.domain.mapper.dtos.ArticleDto;
import ch.skaldenmagic.cardmarket.autopricing.domain.model.Card;
import ch.skaldenmagic.cardmarket.autopricing.domain.service.ProductService;
import ch.skaldenmagic.cardmarket.autopricing.domain.service.UploadService;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
  private final UploadService uploadService;
  private final ProductService productService;

  @Autowired
  public SorterUploadController(ProductService productService, UploadService uploadService) {
    this.productService = productService;
    this.uploadService = uploadService;
  }

  @GetMapping("/sorterUpload")
  public String main() {
    return "sorterUpload";
  }

  @PostMapping("/upload")
  public ResponseEntity<List<ArticleDto>> uploadFile(@RequestParam("file") MultipartFile file,
      RedirectAttributes attributes) {

    // check if file is empty
    if (file.isEmpty()) {
      return ResponseEntity.badRequest().build();
    }
    List<Card> sorterCards = new ArrayList<>();
    try {
      sorterCards = uploadService.readSorterCSV(file.getBytes());
    } catch (IOException ioException) {
      ioException.printStackTrace();
    }
    List<ArticleDto> uploaderResult = uploadService.prepareUpload(sorterCards);
    return new ResponseEntity<>(uploaderResult, HttpStatus.OK);
  }
}
