package ch.skaldenmagic.cardmarket.autopricing.service.util;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import ch.skaldenmagic.cardmarket.autopricing.domain.entity.ProductEntity;
import ch.skaldenmagic.cardmarket.autopricing.util.FileImport;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.util.Assert;

class FileImportTest {

    @Test
    public void extractingPrices() throws IOException {
        String result = "\"idProduct\",\"Avg. Sell Price\",\"Low Price\",\"Trend Price\",\"German Pro Low\",\"Suggested Price\",\"Foil Sell\",\"Foil Low\",\"Foil Trend\",\"Low Price Ex+\",\"AVG1\",\"AVG7\",\"AVG30\",\"Foil AVG1\",\"Foil AVG7\",\"Foil AVG30\",";
        InputStream resourceAsStream = getClass().getClassLoader().getResourceAsStream("MkmPricefileguideTest.txt");
        assertNotNull(resourceAsStream);
        byte zippedPriceArray[] = FileImport.getPriceGuideFile(resourceAsStream);
        Assert.isTrue(new String(zippedPriceArray).contains(result));
        try (FileOutputStream fos = new FileOutputStream("MkmPricefileguideTest.csv")) {
            fos.write(zippedPriceArray);
        }
    }

    @Test
    public void extractingProducts() throws IOException {
        String result = "\"idProduct\",\"Name\",\"Category ID\",\"Category\",\"Expansion ID\",\"Metacard ID\",\"Date Added\"";
        InputStream resourceAsStream = getClass().getClassLoader().getResourceAsStream("MkmProductsfileTest.txt");
        assertNotNull(resourceAsStream);
        byte zippedPriceArray[] = FileImport.getProductsFile(resourceAsStream);
        Assert.isTrue(new String(zippedPriceArray).contains(result));
        try (FileOutputStream fos = new FileOutputStream("MkmProductsfileTest.csv")) {
            fos.write(zippedPriceArray);
        }
    }


    @Test
    public void readCsv() throws IOException {
        List<ProductEntity> result = FileImport.readSorterProductCSV("MkmProductsfile.csv");
        Assertions.assertEquals(190209,result.size());
    }


}