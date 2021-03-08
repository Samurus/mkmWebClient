package ch.skaldenmagic.cardmarket.autopricing.domain.mapper;

import ch.skaldenmagic.cardmarket.autopricing.domain.entity.ArticleEntity;
import ch.skaldenmagic.cardmarket.autopricing.domain.mapper.dtos.ArticleDto;
import de.cardmarket4j.entity.Article;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {LocalizationMapper.class, ProductMapper.class,
    UserMapper.class},
    injectionStrategy =
        InjectionStrategy.CONSTRUCTOR)
public interface ArticleMapper {

  @Mapping(target = "inShoppingCart", ignore = true)
  @Mapping(target = "product", ignore = true)
  @Mapping(target = "productId", source = "product.productId")
  @Mapping(target = "seller", ignore = true)
  @Mapping(target = "lastEdited", ignore = true)
  @Mapping(target = "firstEdition", ignore = true)
  Article dtoToMkm(ArticleDto articleDto);

  ArticleDto entityToDto(ArticleEntity article);

  @Mapping(target = "product.productId", source = "productId")
  @Mapping(target = "product.name", source = "product.name")
  @Mapping(target = "product.imageUrl", source = "product.imageUrl")
  @Mapping(target = "product.expansionCollectionNumber", source = "product.expansionCollectionNumber")
  @Mapping(target = "product.rarity", source = "product.rarity")
  @Mapping(target = "product.expansionName", source = "product.expansionName")
  ArticleEntity mkmToEntity(Article article);


}