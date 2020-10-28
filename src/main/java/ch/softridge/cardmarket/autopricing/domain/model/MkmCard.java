package ch.softridge.cardmarket.autopricing.domain.model;

import ch.softridge.cardmarket.autopricing.domain.entity.LocalizationEntity;
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

    @OneToMany()
    @JoinColumn(name = "mkmcard_id")
    private Set<LocalizationEntity> localizations;

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
