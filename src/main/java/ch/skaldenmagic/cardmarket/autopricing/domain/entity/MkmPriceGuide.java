package ch.skaldenmagic.cardmarket.autopricing.domain.entity;

import java.math.BigDecimal;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Kevin Zellweger
 * @Date 28.10.20
 * <p>
 * Represents a Subset of the MKM User Object. We ignore with purpose all fields which relates to
 * the Persons Contact information.
 */
@Setter
@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "mkm_priceguide")
public class MkmPriceGuide {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;
  private BigDecimal sell;
  private BigDecimal low;
  private BigDecimal lowExPlus;
  private BigDecimal lowFoil;
  private BigDecimal avg;
  private BigDecimal trend;
}
