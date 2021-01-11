package ch.softridge.cardmarket.autopricing.domain.mapper;

import ch.softridge.cardmarket.autopricing.domain.entity.ArticlePriceEntity;
import ch.softridge.cardmarket.autopricing.domain.mapper.dtos.ArticlePriceDto;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

/**
 * @author Kevin Zellweger
 * @Date 01.11.20
 */
@Mapper(componentModel = "spring")
public interface ArticlePriceMapper {

  ArticlePriceDto toDto(ArticlePriceEntity entity);

  @AfterMapping
  default void calculatePriceDifference(ArticlePriceEntity order, @MappingTarget ArticlePriceDto dto) {
    dto.setPriceDifference(order.getPrice().subtract(order.getRecommendedPrice()));
  }


}
