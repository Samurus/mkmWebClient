package ch.skaldenmagic.cardmarket.autopricing.domain.service;

import ch.skaldenmagic.cardmarket.autopricing.domain.model.Card;
import ch.skaldenmagic.cardmarket.autopricing.domain.repository.CardRepository;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Kevin Zellweger
 * @Date 03.07.20
 */
@Service
public class CardService {

  private static final Logger log = LoggerFactory.getLogger(CardService.class);
  private final CardRepository cardRepository;

  @Autowired
  public CardService(CardRepository cardRepository) {
    this.cardRepository = cardRepository;
  }

  public void addCard(Card card) {
    cardRepository.save(card);
  }

  public List<Card> findAll() {
    return cardRepository.findAll();
  }

  public void deleteAll() {
    cardRepository.deleteAll();
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

  public void addAll(List<Card> cards) {
    cardRepository.saveAll(cards);
  }
}
