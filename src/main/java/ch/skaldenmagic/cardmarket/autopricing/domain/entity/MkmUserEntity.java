package ch.skaldenmagic.cardmarket.autopricing.domain.entity;

import java.time.LocalDateTime;
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
@Table(name = "mkm_user")
public class MkmUserEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;
  private int userId;
  private String userName;
  private LocalDateTime registrationDate;
  private boolean isCommercial;
  private boolean isSeller;
  private String country;
  private int sellCount;
  private int soldItems;
}
