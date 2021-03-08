package ch.skaldenmagic.cardmarket.autopricing.domain.mapper;

import ch.skaldenmagic.cardmarket.autopricing.domain.entity.ExpansionEntity;
import de.cardmarket4j.entity.Expansion;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = LocalizationMapper.class)
public interface ExpansionMapper {

  ExpansionEntity mkmToEntity(Expansion article);
}