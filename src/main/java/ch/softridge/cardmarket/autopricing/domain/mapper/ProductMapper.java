package ch.softridge.cardmarket.autopricing.domain.mapper;

import ch.softridge.cardmarket.autopricing.domain.entity.ProductEntity;
import ch.softridge.cardmarket.autopricing.domain.mapper.dtos.ProductDto;
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
@Mapper(componentModel = "spring", uses = {LocalizationMapper.class,
    ExpansionMapper.class}, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public abstract interface ProductMapper {

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "version", ignore = true)
  @Mapping(target = "listReprintProductIds", ignore = true)
  abstract ProductEntity apiProductToEntity(Product product);

  abstract ProductDto productToProductDto(Product product);


  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  void updateSecondWithFirst(ProductEntity source, @MappingTarget ProductEntity target);


}
