package ch.skaldenmagic.cardmarket.autopricing.domain.mapper.dtos;

import ch.skaldenmagic.cardmarket.autopricing.domain.entity.ExpansionEntity;
import ch.skaldenmagic.cardmarket.autopricing.domain.entity.MkmPriceGuide;
import de.cardmarket4j.entity.enumeration.Game;
import java.util.Set;
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
  private String dateAdded;
  private Integer metaproductId;
  private Integer totalReprints;
  private Set<LocalizationDto> localizations;
  private String selfUrl;
  private String imageUrl;
  private Game game;
  private String expansionCollectionNumber;
  private String rarity;
  private String expansionName;
  private ExpansionEntity expansion;
  private MkmPriceGuide priceGuide;
//  private List<Integer> listReprintProductIds; //List of Products or productIds

}
