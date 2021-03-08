package ch.skaldenmagic.cardmarket.autopricing.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import de.cardmarket4j.entity.enumeration.Game;
import java.time.LocalDate;
import java.util.Objects;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
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
@Table(name = "product", uniqueConstraints = {@UniqueConstraint(columnNames = {"product_id"})})
public class ProductEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;
  @Column(name = "product_id")
  private Integer productId;
  private Integer metaproductId;
  private Integer totalReprints;
  private String name;
  @OneToMany(mappedBy = "product", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JsonIgnoreProperties("product")
  private Set<LocalizationEntity> localizations;
  private Integer categoryId;
  private String categoryName;
  private String selfUrl;
  private String imageUrl;
  private Game game;
  private String expansionCollectionNumber;
  private String rarity;
  private String expansionName;
  private LocalDate dateAdded;
  @OneToOne
  private ExpansionEntity expansion;
  @OneToOne
  private MkmPriceGuide priceGuide;
  //TODO implement reprint relation table
//  @OneToMany
//  @JoinColumn(name = "reprint_product_id")
//  private List<ProductReprintEntity> listReprintProductIds;

  //todo: delegate to service
  public ProductEntity(String[] sorterResult) {
    this.productId = Integer.valueOf(sorterResult[0]);
    this.name = sorterResult[1];
    this.categoryId = Integer.valueOf(sorterResult[2]);
    this.categoryName = sorterResult[3];
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ProductEntity that = (ProductEntity) o;
    return productId.equals(that.productId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), productId);
  }
}
