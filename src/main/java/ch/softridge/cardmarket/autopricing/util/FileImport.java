package ch.softridge.cardmarket.autopricing.util;

import ch.softridge.cardmarket.autopricing.domain.entity.ProductEntity;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.tomcat.util.codec.binary.Base64;
import org.h2.util.IOUtils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.zip.GZIPInputStream;

public class FileImport {

    private static String readFromInputStream(InputStream inputStream)
            throws IOException {
        StringBuilder resultStringBuilder = new StringBuilder();
        try (BufferedReader br
                     = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = br.readLine()) != null) {
                resultStringBuilder.append(line).append("\n");
            }
        }
        return resultStringBuilder.toString();
    }

    private static byte[] decompress(byte[] contentBytes) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            IOUtils.copy(new GZIPInputStream(new ByteArrayInputStream(contentBytes)), out);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return out.toByteArray();
    }

    public static byte[] getPriceGuideFile(InputStream resourceAsStream) throws IOException {
        return decompress(
                Base64.decodeBase64(
                        ((JsonObject) JsonParser.parseString(
                                readFromInputStream(resourceAsStream)))
                                .get("priceguidefile").toString()));
    }

    public static byte[] getProductsFile(InputStream resourceAsStream) throws IOException {
        return decompress(
                Base64.decodeBase64(
                        ((JsonObject) JsonParser.parseString(
                                readFromInputStream(resourceAsStream)))
                                .get("productsfile").toString()));
    }

    public static byte[] decompressBase64(InputStream inputStream) throws IOException{
        return decompress(Base64.decodeBase64(readFromInputStream(inputStream)));
    }

    public static List<ProductEntity> readSorterProductCSV(String fileName) throws IOException {
        try (BufferedReader reader = Files.newBufferedReader(Paths.get(fileName))) {
            return reader.lines()
                    .skip(1)
                    .map(line -> line.replaceFirst("\"", ""))
                    .map(line -> line.replaceAll("\"\"\"", "\"")) //14879,"""Ach! Hans, Run!""","1","Magic Single","59","8868","2007-01-01 00:00:00"
                    .map(line -> line.replaceAll("\"\"", "\"-1\"")) //null values
                    .map(line -> new ProductEntity(line.split("\",\"", 7)))
                    .collect(Collectors.toList());
        }
    }

}
