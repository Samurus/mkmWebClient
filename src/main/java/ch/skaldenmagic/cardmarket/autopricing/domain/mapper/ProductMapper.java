package ch.skaldenmagic.cardmarket.autopricing.domain.mapper;

import ch.skaldenmagic.cardmarket.autopricing.domain.entity.ProductEntity;
import ch.skaldenmagic.cardmarket.autopricing.domain.mapper.dtos.ProductDto;
import de.cardmarket4j.entity.Product;
import org.mapstruct.BeanMapping;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

/**
 * @author Kevin Zellweger
 * @Date 28.10.20
 */
@Mapper(componentModel = "spring", uses = {
    ExpansionMapper.class}, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface ProductMapper {

  ProductDto dtoToMkm(Product product);

  ProductDto entityToDto(ProductEntity productEntity);

  @Mapping(target = "localizations", ignore = true)
  ProductEntity mkmToEntity(Product product);

  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  void updateSecondWithFirst(ProductEntity source, @MappingTarget ProductEntity target);

}
