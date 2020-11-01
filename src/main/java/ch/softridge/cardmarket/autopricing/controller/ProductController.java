package ch.softridge.cardmarket.autopricing.controller;

import ch.softridge.cardmarket.autopricing.domain.entity.ProductEntity;
import ch.softridge.cardmarket.autopricing.domain.service.ProductService;
import java.io.IOException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Kevin Zellweger
 * @Date 28.10.20
 */
@RestController
@RequestMapping("/product")
public class ProductController {

  @Autowired
  private ProductService productService;


  @GetMapping("/import")
  public List<ProductEntity> persistProductFile() throws IOException {
    return productService.persistProductFile();
  }


}
