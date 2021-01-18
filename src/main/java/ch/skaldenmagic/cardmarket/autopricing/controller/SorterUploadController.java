package ch.skaldenmagic.cardmarket.autopricing.controller;

import ch.skaldenmagic.cardmarket.autopricing.domain.mapper.dtos.ArticleDto;
import ch.skaldenmagic.cardmarket.autopricing.domain.model.Card;
import ch.skaldenmagic.cardmarket.autopricing.domain.service.ProductService;
import ch.skaldenmagic.cardmarket.autopricing.domain.service.StockService;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

/**
 * Controller for uploading CSV-Files to the Application The CSV-Files must respect the format of
 * the Output from {@see https://www.magic-sorter.com/}
 *
 * @author Kevin Zellweger
 * @Date 05.07.20
 */
@Controller
@RequestMapping("/upload")
public class SorterUploadController {

  private static final Logger log = LoggerFactory.getLogger(SorterUploadController.class);
  private final String UPLOAD_DIR = "./uploads/";
  private final UploadService uploadService;
  private final ProductService productService;
  private final StockService stockService;

  @Autowired
  public SorterUploadController(ProductService productService, UploadService uploadService,
      StockService stockService) {
    this.productService = productService;
    this.uploadService = uploadService;
    this.stockService = stockService;
  }

  @PostMapping("/tomkm")
  public ResponseEntity<List<ArticleDto>> postToMkm(
      @RequestBody List<ArticleDto> articleDtos) {
    System.out.println(articleDtos);
    return new ResponseEntity<>(stockService.postNewArticlesToStock(articleDtos), HttpStatus.OK);
  }

  /**
   * Read a CSV file and create new Article entities. This is done Best-Effort. We search Products
   * by their EN-Name and the Set Code. Products not found are returned as "Unknown".
   *
   * @param file csv-File
   * @return List of created Articles as DTO
   */
  @PostMapping("/csv")
  public ResponseEntity<List<ArticleDto>> uploadFile(@RequestParam("file") MultipartFile file) {

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
