package ch.softridge.cardmarket.autopricing.repository.model;

import com.neovisionaries.i18n.LanguageCode;
import de.cardmarket4j.entity.enumeration.Condition;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;


@Setter
@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name="article")
public class ArticleEntity extends BaseEntity {

        private int articleId;
        private Integer productId;
        private LanguageCode languageCode;
        private String comment;
        private BigDecimal price;
        private int quantity; //count
        private boolean inShoppingCart;
        //	private Product product;
//	private User seller;
        private String seller; //TODO User-Datenbank erstellen
        //	private LocalDateTime lastEdited; //"2020-10-12T16:41:37+0200"
        private Condition condition;
        private boolean foil;
        private boolean signed;
        private boolean altered;
        private boolean playset;
        private boolean firstEdition; //only Yu-Gi-Oh! https://api.cardmarket.com/ws/documentation/API_2.0:Stock

}