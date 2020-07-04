package ch.softridge.cardmarket.autopricing.service;

import ch.softridge.cardmarket.autopricing.model.ScryfallCard;
import ch.softridge.cardmarket.autopricing.repository.ScryfallCardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Kevin Zellweger
 * @Date 04.07.20
 */
@Service
public class ScryfallCardService {
    ScryfallCardRepository cardRepository;

    @Autowired
    public ScryfallCardService(ScryfallCardRepository cardRepository){
        this.cardRepository = cardRepository;
    }

    public void addCard(ScryfallCard card){
        cardRepository.save(card);
    }

    public void deleteAll() {
        cardRepository.deleteAll();
    }

    public List<ScryfallCard> findAll(){
       return cardRepository.findAll();
    }

}
