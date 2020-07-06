package ch.softridge.cardmarket.autopricing.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * @author Kevin Zellweger
 * @Date 04.07.20
 */
@Entity
@Getter @Setter
public class Localization {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private int idLanguage;
    private Language language;
    private String productName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="mkmCard_id",referencedColumnName = "id")
    private MkmCard mkmCard;

    public Localization(int idLanguage, Language language, String productName) {
        this.idLanguage = idLanguage;
        this.language = language;
        this.productName = productName;
    }

    protected Localization(){}
}
