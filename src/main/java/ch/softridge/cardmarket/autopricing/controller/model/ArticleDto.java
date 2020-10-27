package ch.softridge.cardmarket.autopricing.controller.model;

import ch.softridge.cardmarket.autopricing.repository.model.ArticlePrice;
import com.neovisionaries.i18n.LanguageCode;
import de.cardmarket4j.entity.enumeration.Condition;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ArticleDto {
        private int articleId;
        private Integer productId;
        private LanguageCode languageCode;
        private String comment;
        private BigDecimal price;
        private int quantity;
        private boolean inShoppingCart;
        //	private Product product;
//	private User seller;
        private String seller; //TODO User-Datenbank erstellen
        //	private LocalDateTime lastEdited;
        private Condition condition;
        private boolean foil;
        private boolean signed;
        private boolean altered;
        private boolean playset;
        private boolean firstEdition;

        private ArticlePrice articlePrice;

}