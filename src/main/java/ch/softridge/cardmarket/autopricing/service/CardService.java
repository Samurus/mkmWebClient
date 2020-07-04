package ch.softridge.cardmarket.autopricing.service;

import ch.softridge.cardmarket.autopricing.model.Card;
import ch.softridge.cardmarket.autopricing.repository.CardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Kevin Zellweger
 * @Date 03.07.20
 */
@Service
public class CardService {

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
}
