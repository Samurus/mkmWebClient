package ch.softridge.cardmarket.autopricing.service.mapper;

import ch.softridge.cardmarket.autopricing.controller.model.ArticleDto;
import ch.softridge.cardmarket.autopricing.repository.model.ArticleEntity;
import de.cardmarket4j.entity.Article;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface ArticleMapper {

    @Mapping(target = "version", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "seller", source = "seller.userName")
    ArticleEntity articleToEntity(Article article);


    ArticleDto articleEntityToDto(ArticleEntity article);

}