package ch.skaldenmagic.cardmarket.autopricing.domain.entity;

import de.cardmarket4j.entity.enumeration.Game;
import java.time.LocalDateTime;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
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
@Table(name = "expansion", uniqueConstraints = {@UniqueConstraint(columnNames = {"expansion_id"})})
public class ExpansionEntity extends BaseEntity {

  @OneToMany()
  @JoinColumn(name = "localization_id")
  Set<LocalizationEntity> localizations;

  @Column(name = "expansion_id")
  private Integer expansionId;
  private String name;
  private String code;
  private Integer iconCode;
  private LocalDateTime releaseDate;
  private Game game;

}
