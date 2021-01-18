package ch.skaldenmagic.cardmarket.autopricing.domain.mapper;

import ch.skaldenmagic.cardmarket.autopricing.domain.entity.LocalizationEntity;
import ch.skaldenmagic.cardmarket.autopricing.domain.entity.ProductEntity;
import com.neovisionaries.i18n.LanguageCode;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.Mapper;

/**
 * @author Kevin Zellweger
 * @Date 28.10.20
 */
@Mapper(componentModel = "spring")
public interface LocalizationMapper {

  default Set<LocalizationEntity> mapToLocalization(Map<LanguageCode, String> languageMapping,
      ProductEntity parent) {
    return languageMapping.entrySet()
        .stream()
        .map(entrySet -> new LocalizationEntity(entrySet.getKey(), entrySet.getValue(), parent))
        .collect(Collectors.toSet());
  }
}
