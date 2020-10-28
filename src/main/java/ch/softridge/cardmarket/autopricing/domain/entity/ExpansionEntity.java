package ch.softridge.cardmarket.autopricing.domain.entity;

import de.cardmarket4j.entity.enumeration.Game;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDateTime;


@Setter
@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name="expansion")
public class ExpansionEntity extends BaseEntity {

        private int expansionId;
        private String name;
//        private Map<LanguageCode, String> mapLocalizedNames;
        String mapLocalizedNames;
        private String code;
        private Integer iconCode;
        private LocalDateTime releaseDate;
        private Game game;

}