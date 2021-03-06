package ch.softridge.cardmarket.autopricing.domain.entity;

import de.cardmarket4j.entity.enumeration.Game;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
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
public class ProductEntity extends BaseEntity {

  @Column(name = "product_id")
  private Integer productId;
  private Integer metaproductId;
  private Integer totalReprints;
  private String name;
  @OneToMany
  @JoinColumn(name = "localization_id")
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
  @OneToMany
  @JoinColumn(name = "reprint_product_id")
  private List<ProductReprintEntity> listReprintProductIds;

  //todo: delegate to service
  public ProductEntity(String[] sorterResult) {
    this.productId = Integer.valueOf(sorterResult[0]);
    this.name = sorterResult[1];
    this.categoryId = Integer.valueOf(sorterResult[2]);
    this.categoryName = sorterResult[3];
    //this.expansionId = Integer.valueOf(sorterResult[4]);
    //this.metaCardId = Integer.valueOf(sorterResult[5]);
    //this.dateAdded = sorterResult[6].replaceAll("\"","");
  }

}
