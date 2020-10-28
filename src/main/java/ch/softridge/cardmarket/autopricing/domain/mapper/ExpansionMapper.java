package ch.softridge.cardmarket.autopricing.domain.mapper;

import ch.softridge.cardmarket.autopricing.domain.entity.ExpansionEntity;
import de.cardmarket4j.entity.Expansion;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ExpansionMapper {

    @Mapping(target = "version", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "mapLocalizedNames",  ignore = true) //TODO one to many with localization table for expansions
    ExpansionEntity toEntity(Expansion article);


}