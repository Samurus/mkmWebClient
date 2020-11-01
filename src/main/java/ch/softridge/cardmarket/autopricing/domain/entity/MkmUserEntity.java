package ch.softridge.cardmarket.autopricing.domain.entity;

import java.time.LocalDateTime;
import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
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
public class MkmUserEntity extends BaseEntity {

  private int userId;
  private String userName;
  private LocalDateTime registrationDate;
  private boolean isCommercial;
  private boolean isSeller;
  private String country;
  private int sellCount;
  private int soldItems;
  @OneToMany
  private Set<ArticleEntity> knownArticles;


}
