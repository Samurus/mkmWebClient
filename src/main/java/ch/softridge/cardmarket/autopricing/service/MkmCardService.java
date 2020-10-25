package ch.softridge.cardmarket.autopricing.service;

import ch.softridge.cardmarket.autopricing.service.model.MkmCard;
import ch.softridge.cardmarket.autopricing.repository.MkmCardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Kevin Zellweger
 * @Date 04.07.20
 */
@Service
public class MkmCardService {
    MkmCardRepository cardRepository;

    @Autowired
    public MkmCardService(MkmCardRepository cardRepository){
        this.cardRepository = cardRepository;
    }

    public void addCard(MkmCard card){
        cardRepository.save(card);
    }

    public void deleteAll() {
        cardRepository.deleteAll();
    }
    public List<MkmCard> findAll(){
        return cardRepository.findAll();
    }
}
