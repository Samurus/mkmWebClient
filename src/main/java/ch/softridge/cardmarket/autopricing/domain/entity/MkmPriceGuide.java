package ch.softridge.cardmarket.autopricing.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;

/**
 * @author Kevin Zellweger
 * @Date 28.10.20
 *
 * Represents a Subset of the MKM User Object.
 * We ignore with purpose all fields which relates to the Persons Contact information.
 */
@Setter
@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "mkm_priceguide")
public class MkmPriceGuide extends BaseEntity {
    private BigDecimal sell;
    private BigDecimal low;
    private BigDecimal lowExPlus;
    private BigDecimal lowFoil;
    private BigDecimal avg;
    private BigDecimal trend;
}
