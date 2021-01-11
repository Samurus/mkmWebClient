package ch.skaldenmagic.cardmarket.autopricing.domain.mapper;

import ch.skaldenmagic.cardmarket.autopricing.domain.entity.LocalizationEntity;
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
public abstract class LocalizationMapper {

  Set<LocalizationEntity> mapToLocalization(Map<LanguageCode, String> languageMapping) {
    return languageMapping.entrySet()
        .stream()
        .map(entrySet -> new LocalizationEntity(entrySet.getKey(), entrySet.getValue()))
        .collect(Collectors.toSet());
  }
}
