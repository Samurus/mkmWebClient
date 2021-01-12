package ch.skaldenmagic.cardmarket.autopricing.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.neovisionaries.i18n.LanguageCode;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
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
@AllArgsConstructor
@NoArgsConstructor
public class LocalizationEntity extends BaseEntity {

  private LanguageCode language;
  private String productName;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "product_id", nullable = false)
  @JsonIgnoreProperties("product")
  private ProductEntity product;

}
