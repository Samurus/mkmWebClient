package ch.skaldenmagic.cardmarket.autopricing.domain.service;

import ch.skaldenmagic.cardmarket.autopricing.domain.entity.MkmPriceGuide;
import ch.skaldenmagic.cardmarket.autopricing.domain.repository.MkmPriceGuideRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Kevin Zellweger
 * @Date 11.03.21
 */
@Service
public class MkmPriceGuideService {

  @Autowired
  MkmPriceGuideRepository repository;

  public MkmPriceGuide save(MkmPriceGuide e) {
    return repository.save(e);
  }

}
