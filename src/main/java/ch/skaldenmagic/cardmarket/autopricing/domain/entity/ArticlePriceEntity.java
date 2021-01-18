package ch.skaldenmagic.cardmarket.autopricing.domain.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.math.BigDecimal;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "article_price")
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ArticlePriceEntity extends BaseEntity {

  private Integer articleId;
  private BigDecimal recommendedPrice;
  private BigDecimal price;
}
