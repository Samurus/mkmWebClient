package ch.softridge.cardmarket.autopricing.domain.service;

import ch.softridge.cardmarket.autopricing.domain.entity.ExpansionEntity;
import ch.softridge.cardmarket.autopricing.domain.mapper.ExpansionMapper;
import ch.softridge.cardmarket.autopricing.domain.repository.ExpansionRepository;
import de.cardmarket4j.entity.Expansion;
import de.cardmarket4j.entity.util.ProductFilter;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Kevin Zellweger
 * @Date 31.10.20
 */

@Service
@Transactional
public class ExpansionServie {

  @Autowired
  MkmService mkmService;
  @Autowired
  ExpansionMapper expansionMapper;
  @Autowired
  private ExpansionRepository expansionRepository;

  public ExpansionEntity getByExpansionId(Integer expansionId) {
    return expansionRepository.getByExpansionId(expansionId);
  }

  public List<ExpansionEntity> findAllByNameContaining(String expansionName) {
    return expansionRepository.findAllByNameContaining(expansionName);
  }

  public List<ExpansionEntity> persistExpansions() throws IOException {

    Set<Expansion> expansions = mkmService.getCardMarket().getMarketplaceService()
        .getExpansions(new ProductFilter("?"));
    List<ExpansionEntity> entities = expansions.stream().map(expansionMapper::mkmToEntity)
        .collect(Collectors.toList());
    return expansionRepository.saveAll(entities);
  }

}
