package ch.softridge.cardmarket.autopricing.service;

import ch.softridge.cardmarket.autopricing.repository.ProductRepository;
import ch.softridge.cardmarket.autopricing.repository.model.ProductEntity;
import ch.softridge.cardmarket.autopricing.service.util.FileImport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Service
@Transactional
public class ProductService {

    private static Logger log = LoggerFactory.getLogger(ProductService.class);

    @Autowired
    private ProductRepository productRepository;

    public List<ProductEntity> persistProductFile() throws IOException {
        productRepository.deleteAll();
        //TODO replace file with rest-request to mkm
        InputStream resourceAsStream = getClass().getClassLoader().getResourceAsStream("MkmProductsfile.txt");
        byte[] zippedPriceArray = FileImport.getProductsFile(resourceAsStream);
        try (FileOutputStream fos = new FileOutputStream("MkmProductsfile.csv")) {
            fos.write(zippedPriceArray);
        }
        List<ProductEntity> productEntities = FileImport.readSorterProductCSV("MkmProductsfile.csv");
        return productRepository.saveAll(productEntities);
    }


}
