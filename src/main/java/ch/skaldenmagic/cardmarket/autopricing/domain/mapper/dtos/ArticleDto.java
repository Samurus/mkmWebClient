package ch.skaldenmagic.cardmarket.autopricing.domain.mapper.dtos;

import com.neovisionaries.i18n.LanguageCode;
import de.cardmarket4j.entity.User;
import de.cardmarket4j.entity.enumeration.Condition;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ArticleDto {

  private Integer articleId;
  private LanguageCode languageCode;
  private String comment;
  private BigDecimal price;
  private int quantity;
  private boolean inShoppingCart;
  private ProductDto product;
  private User seller;
  private LocalDateTime lastEdited;
  private Condition condition;
  private boolean foil;
  private boolean signed;
  private boolean altered;
  private boolean playset;
  private boolean firstEdition;

  private ArticlePriceDto articlePriceEntity;

}
