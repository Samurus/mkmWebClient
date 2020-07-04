package ch.softridge.cardmarket.autopricing.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * @author Kevin Zellweger
 * @Date 29.06.20
 */
@Entity
@Getter @Setter
@Table(uniqueConstraints={@UniqueConstraint(columnNames={"title","set"})})
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id; //Unique Id in our own DB

    private String set;
    private String title;
    private Rarity rarity;
    private int count;
    private double price;
    private double price_trend;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(unique = true)
    private MkmCard mkmCard;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(unique = true)
    private ScryfallCard scryfallCard;

    public Card(String title, String set, Rarity rarity, int count, double price, double price_trend){
        this.title = title;
        this.set = set;
        this.rarity = rarity;
        this.count = count;
        this.price = price;
        this.price_trend = price_trend;
    }
    protected Card(){}


}
