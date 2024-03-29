package ch.softridge.cardmarket.autopricing.domain.entity;

import com.neovisionaries.i18n.LanguageCode;
import de.cardmarket4j.entity.enumeration.Condition;
import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Setter
@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "article")
public class ArticleEntity extends BaseEntity {

  private int articleId;
  //private Integer productId;
  //can be represented within the ProductEntity and is redundant
  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  @JoinColumn(name = "product_id")
  private ProductEntity product;

  private LanguageCode languageCode;
  private String comment;
  //private BigDecimal price;
  //price is represented by articlePriceEntity
  @OneToOne(optional = false)
  private ArticlePriceEntity articlePrice;
  private int quantity; //count
  private boolean inShoppingCart;
  @ManyToOne()
  @JoinColumn(name = "mkm_user_id")
  private MkmUserEntity seller;
  private LocalDateTime lastEdited; //"2020-10-12T16:41:37+0200"
  private Condition condition;
  private boolean foil;
  private boolean signed;
  private boolean altered;
  private boolean playset;
  private boolean firstEdition; //only Yu-Gi-Oh! https://api.cardmarket.com/ws/documentation/API_2.0:Stock

}