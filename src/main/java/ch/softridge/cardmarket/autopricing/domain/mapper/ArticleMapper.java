package ch.softridge.cardmarket.autopricing.domain.mapper;

import ch.softridge.cardmarket.autopricing.controller.model.ArticleDto;
import ch.softridge.cardmarket.autopricing.domain.entity.ArticleEntity;
import de.cardmarket4j.entity.Article;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ArticleMapper {

    @Mapping(target = "version", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "articlePrice", ignore = true)
    @Mapping(target = "seller", source = "seller.userName")
    ArticleEntity articleToEntity(Article article);


    ArticleDto articleEntityToDto(ArticleEntity article);

    @Mapping(target = "inShoppingCart", ignore = true)
    @Mapping(target = "product", ignore = true)
    @Mapping(target = "seller", ignore = true)
    @Mapping(target = "lastEdited", ignore = true)
    @Mapping(target = "firstEdition", ignore = true)
    Article dtoToArticle(ArticleDto articleDto);

}