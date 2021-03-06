package ch.softridge.cardmarket.autopricing.domain.entity;

import com.neovisionaries.i18n.LanguageCode;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Kevin Zellweger
 * @Date 04.07.20
 */
@Getter
@Setter
@Entity
@Table(name = "localization")
@AllArgsConstructor
@NoArgsConstructor
public class LocalizationEntity extends BaseEntity {

  private LanguageCode language;
  private String productName;


}
