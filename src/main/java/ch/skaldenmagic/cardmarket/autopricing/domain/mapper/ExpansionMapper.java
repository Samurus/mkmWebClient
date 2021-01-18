package ch.skaldenmagic.cardmarket.autopricing.domain.mapper;

import ch.skaldenmagic.cardmarket.autopricing.domain.entity.ExpansionEntity;
import de.cardmarket4j.entity.Expansion;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = LocalizationMapper.class)
public interface ExpansionMapper {

  @Mapping(target = "version", ignore = true)
  @Mapping(target = "id", ignore = true)
  ExpansionEntity mkmToEntity(Expansion article);
}