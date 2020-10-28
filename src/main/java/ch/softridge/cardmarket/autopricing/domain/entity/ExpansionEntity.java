package ch.softridge.cardmarket.autopricing.domain.entity;

import de.cardmarket4j.entity.enumeration.Game;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.Set;


@Setter
@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "expansion")
public class ExpansionEntity extends BaseEntity {
    @OneToMany()
    @JoinColumn(name = "localization_id")
    Set<LocalizationEntity> localizations;

    private Integer expansionId;
    private String name;
    private String code;
    private Integer iconCode;
    private LocalDateTime releaseDate;
    private Game game;

}