package ch.skaldenmagic.cardmarket.autopricing.controller.endpoint;

import ch.skaldenmagic.cardmarket.autopricing.domain.service.ProductService;
import java.io.IOException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Kevin Zellweger
 * @Date 05.07.20
 */
@RestController
@RequestMapping("/product")
public class ProductEndpoint {

  @Autowired
  private ProductService productService;

  @GetMapping("/expansion/name")
  public List<String> findExpansionsByName()
      throws IOException {
    return productService.findAllExpansionNames();
  }

}
