package ch.skaldenmagic.cardmarket.autopricing.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.neovisionaries.i18n.LanguageCode;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Kevin Zellweger
 * @Date 04.07.20
 */
@Getter
@Setter
@Entity
@Table(name = "localization")
@NoArgsConstructor
public class LocalizationEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;
  private LanguageCode language;
  private String productName;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "product_id", nullable = false)
  @JsonIgnoreProperties("product")
  private ProductEntity product;

  public LocalizationEntity(LanguageCode language, String productName,
      ProductEntity product) {
    this.language = language;
    this.productName = productName;
    this.product = product;
  }
}
