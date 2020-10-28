package ch.softridge.cardmarket.autopricing.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;

@Setter
@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "product")
public class ProductEntity extends BaseEntity {

    private Integer productId;
    private String name;
    private Integer categoryId;
    private String categoryName;
    private Integer expansionId;
    private Integer metaCardId;
    private String dateAdded;

    public ProductEntity(String[] sorterResult) {
        this.productId = Integer.valueOf(sorterResult[0]);
        this.name = sorterResult[1];
        this.categoryId = Integer.valueOf(sorterResult[2]);
        this.categoryName = sorterResult[3];
        this.expansionId = Integer.valueOf(sorterResult[4]);
        this.metaCardId = Integer.valueOf(sorterResult[5]);
        this.dateAdded = sorterResult[6].replaceAll("\"","");
    }

}
