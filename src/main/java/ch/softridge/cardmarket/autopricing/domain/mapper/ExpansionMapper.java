package ch.softridge.cardmarket.autopricing.domain.mapper;

import ch.softridge.cardmarket.autopricing.domain.entity.ExpansionEntity;
import de.cardmarket4j.entity.Expansion;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = LocalizationMapper.class)
public interface ExpansionMapper {

  @Mapping(target = "version", ignore = true)
  @Mapping(target = "id", ignore = true)
  ExpansionEntity toEntity(Expansion article);
}