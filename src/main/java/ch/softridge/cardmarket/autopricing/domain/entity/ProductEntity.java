package ch.softridge.cardmarket.autopricing.domain.entity;

import de.cardmarket4j.entity.enumeration.Game;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.util.List;
import java.util.Set;

@Setter
@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "product")
public class ProductEntity extends BaseEntity {

    private Integer productId;
    private Integer metaproductId;
    private Integer totalReprints;
    private String name;
    @OneToMany
    private Set<LocalizationEntity> localizations;
    private Integer categoryId;
    private String categoryName;
    private String selfUrl;
    private String imageUrl;
    private Game game;
    private String expansionCollectionNumber;
    private String rarity;
    private String expansionName;
    @OneToOne
    private ExpansionEntity expansion;
    @OneToOne
    private MkmPriceGuide priceGuide;
    @OneToMany
    private List<ProductEntity> listReprintProductIds;

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
