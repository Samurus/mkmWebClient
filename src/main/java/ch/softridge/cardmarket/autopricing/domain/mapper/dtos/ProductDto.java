package ch.softridge.cardmarket.autopricing.domain.mapper.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ProductDto {
    private Integer productId;
    private String name;
    private Integer categoryId;
    private String categoryName;
    private Integer expansionId;
    private Integer metaCardId;
    private String dateAdded;

}
