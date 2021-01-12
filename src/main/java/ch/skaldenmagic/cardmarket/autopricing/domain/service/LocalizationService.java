package ch.skaldenmagic.cardmarket.autopricing.domain.service;

import ch.skaldenmagic.cardmarket.autopricing.domain.entity.LocalizationEntity;
import ch.skaldenmagic.cardmarket.autopricing.domain.repository.LocalizationRepository;
import java.util.HashSet;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Kevin Zellweger
 * @Date 12.01.21
 */

@Service
public class LocalizationService {

  private final LocalizationRepository localizationRepository;

  @Autowired
  public LocalizationService(
      LocalizationRepository localizationRepository) {
    this.localizationRepository = localizationRepository;
  }

  public HashSet<LocalizationEntity> saveAll(Set<LocalizationEntity> localizations) {
    return new HashSet<>(localizationRepository.saveAll(localizations));
  }

}
