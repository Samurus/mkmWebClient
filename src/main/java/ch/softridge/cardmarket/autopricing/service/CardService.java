package ch.softridge.cardmarket.autopricing.service;

import ch.softridge.cardmarket.autopricing.model.Card;
import ch.softridge.cardmarket.autopricing.repository.CardRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Kevin Zellweger
 * @Date 03.07.20
 */
@Service
public class CardService {

    private static Logger log = LoggerFactory.getLogger(CardService.class);
    private CardRepository cardRepository;

    @Autowired
    public CardService(CardRepository cardRepository){
        this.cardRepository = cardRepository;
    }

    public void addCard(Card card){
        cardRepository.save(card);
    }

    public List<Card> findAll(){
        return cardRepository.findAll();
    }

    public void deleteAll(){
        cardRepository.deleteAll();
    }

    public List<Card> readSorterCSV(String fileName) {
        try (BufferedReader reader = Files.newBufferedReader(Paths.get(fileName))) {
            return reader.lines()
                    .skip(1)
                    .map(line -> new Card(line.split(",", 6)))
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new IllegalStateException("failed");
        }
    }

    public void addAll(List<Card> cards) {
        cardRepository.saveAll(cards);
    }
}
