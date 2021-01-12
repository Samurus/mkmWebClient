package ch.skaldenmagic.cardmarket.autopricing.domain.service;

import ch.skaldenmagic.cardmarket.autopricing.domain.entity.ArticleEntity;
import ch.skaldenmagic.cardmarket.autopricing.domain.model.Card;
import ch.skaldenmagic.cardmarket.autopricing.domain.repository.CardRepository;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * This Service handles the Upload to MKM from a CSV file. The CSV-Structure is implemented to
 * support {@see https://www.magic-sorter.com}
 *
 * @author Kevin Zellweger
 * @Date 03.07.20
 */
@Service
public class UploadService {

  private static final Logger log = LoggerFactory.getLogger(UploadService.class);
  private final CardRepository cardRepository;
  private final ProductService productService;
  private final ArticleService articleService;

  @Autowired
  public UploadService(CardRepository cardRepository, ProductService productService,
      ArticleService articleService) {
    this.cardRepository = cardRepository;
    this.productService = productService;
    this.articleService = articleService;
  }

  public void addAll(List<Card> cards) {
    cardRepository.saveAll(cards);
  }

  public void addCard(Card card) {
    cardRepository.save(card);
  }

  /**
   * Search the Local Article Database and return a List of possible existing Articles. Only
   * available Request criteria are EN-Name and Set. Therefore it is not necessary the correct
   * Article.
   *
   * @param sorterResult Parsed CSV-Data
   * @return possible Articles
   */
  public List<ArticleEntity> alreadyKnownArticles(List<Card> sorterResult) {
    List<ArticleEntity> existingArticles = new ArrayList<>();

    return existingArticles;
  }

  public void deleteAll() {
    cardRepository.deleteAll();
  }

  public List<Card> findAll() {
    return cardRepository.findAll();
  }

  public List<Card> readSorterCSV(byte[] content) {
    try (BufferedReader reader = new BufferedReader(
        new InputStreamReader(new ByteArrayInputStream(content)))) {
      return reader.lines()
          .skip(1)
          .map(line -> new Card(line.split(",", 6)))
          .collect(Collectors.toList());
    } catch (IOException e) {
      log.error(e.getMessage());
      e.printStackTrace();
      throw new IllegalStateException("failed");
    }
  }
}
