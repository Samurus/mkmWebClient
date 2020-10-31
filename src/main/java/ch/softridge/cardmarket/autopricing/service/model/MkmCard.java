package ch.softridge.cardmarket.autopricing.service.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

/**
 * @author Kevin Zellweger
 * @Date 04.07.20
 */
@Entity
@Getter @Setter
public class MkmCard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id; //Unique Id in our own DB
    @Column(unique = true)
    private long idProduct;
    private long idMetaproduct;
    private int countReprints;
    private String enName;

    @OneToOne(mappedBy = "mkmCard")
    private Card card;

    @OneToMany(mappedBy = "mkmCard",
            cascade = CascadeType.PERSIST,
            fetch = FetchType.LAZY)
    private Set<Localization> localizations;

    public MkmCard(long idProduct, long idMetaproduct, int countReprints, String enName, Card card) {
        this.idProduct = idProduct;
        this.idMetaproduct = idMetaproduct;
        this.countReprints = countReprints;
        this.enName = enName;
        this.card = card;
    }

    protected MkmCard() {
    }
}
