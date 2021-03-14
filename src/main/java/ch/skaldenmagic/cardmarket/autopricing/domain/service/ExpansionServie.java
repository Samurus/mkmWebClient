package ch.skaldenmagic.cardmarket.autopricing.domain.service;

import ch.skaldenmagic.cardmarket.autopricing.domain.entity.ExpansionEntity;
import ch.skaldenmagic.cardmarket.autopricing.domain.mapper.ExpansionMapper;
import ch.skaldenmagic.cardmarket.autopricing.domain.repository.ExpansionRepository;
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

  public List<ExpansionEntity> findAll() {
    return expansionRepository.findAll();
  }

  public List<ExpansionEntity> findAllByNameContaining(String expansionName) {
    return expansionRepository.findAllByNameContaining(expansionName);
  }

  public ExpansionEntity getByCode(String expansionCode) {
    return expansionRepository.findByCode(expansionCode);
  }

  public ExpansionEntity getByExpansionId(Integer expansionId) {
    return expansionRepository.getByExpansionId(expansionId);
  }

  public List<ExpansionEntity> persistExpansions() throws IOException {

    Set<Expansion> expansions = mkmService.getCardMarket().getMarketplaceService()
        .getExpansions(new ProductFilter("?"));
    List<ExpansionEntity> entities = expansions.stream().map(expansionMapper::mkmToEntity)
        .collect(Collectors.toList());
    return expansionRepository.saveAll(entities);
  }

  public ExpansionEntity save(ExpansionEntity expansionEntity) {
    return expansionRepository.save(expansionEntity);
  }

  public List<ExpansionEntity> updateExpansionDB() throws IOException {
    Set<Expansion> expansions = mkmService.getCardMarket().getMarketplaceService()
        .getExpansions(new ProductFilter("games/1"));
    List<ExpansionEntity> entities = expansions.stream().map(expansionMapper::mkmToEntity)
        .collect(Collectors.toList());
    List<ExpansionEntity> existingEntities = findAll();
    entities.removeAll(existingEntities);
    entities = expansionRepository.saveAll(entities);
    expansionRepository.flush();
    return entities;
  }

}
