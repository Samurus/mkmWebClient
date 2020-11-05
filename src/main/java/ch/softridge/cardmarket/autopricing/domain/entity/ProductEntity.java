package ch.softridge.cardmarket.autopricing.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import de.cardmarket4j.entity.enumeration.Game;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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


  @JsonIgnore
  @OneToMany(mappedBy = "product", fetch = FetchType.LAZY, orphanRemoval = false)
  private Collection<ArticleEntity> articleEntities = new ArrayList<>();

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

}
