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
public class Prices {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private double usd;
    private double usd_foil;
    private double eur;
    private double tix;

    @OneToOne(mappedBy = "prices")
    private ScryfallCard scryfallCard;

    protected Prices(){}
}
