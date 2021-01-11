package ch.skaldenmagic.cardmarket.autopricing.domain.mapper.dtos;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ArticlePriceDto  {

  private Integer articleId;
  private BigDecimal recommendedPrice;
  private BigDecimal price;
  private BigDecimal priceDifference;


}
