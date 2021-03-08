package ch.skaldenmagic.cardmarket.autopricing.domain.entity;

import com.neovisionaries.i18n.LanguageCode;
import de.cardmarket4j.entity.enumeration.Condition;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Setter
@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "article", uniqueConstraints = {
    @UniqueConstraint(name = "articleId", columnNames = {"articleId"})})
public class ArticleEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  private Integer articleId;
  @ManyToOne(optional = false, fetch = FetchType.EAGER, cascade = {CascadeType.MERGE})
  @JoinColumn(name = "product_id", referencedColumnName = "id", nullable = false)
  private ProductEntity product;

  private LanguageCode languageCode;

  private String comment;

  private BigDecimal price;

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

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ArticleEntity that = (ArticleEntity) o;
    return articleId == that.articleId;
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), articleId);
  }

  public ArticleEntity updateSelf(ArticleEntity entity) {
    this.languageCode = entity.getLanguageCode();
    this.comment = entity.getComment();
    this.price = entity.price;
    this.quantity = entity.quantity;
    this.inShoppingCart = entity.isInShoppingCart();
    this.lastEdited = entity.getLastEdited();
    this.condition = entity.getCondition();
    this.foil = entity.isFoil();
    this.signed = entity.isSigned();
    this.altered = entity.isAltered();
    this.playset = entity.isPlayset();
    this.firstEdition = entity.isFirstEdition();
    return this;
  }
}