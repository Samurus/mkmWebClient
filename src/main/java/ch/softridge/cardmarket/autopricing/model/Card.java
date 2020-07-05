package ch.softridge.cardmarket.autopricing.model;

import javax.persistence.*;

/**
 * @author Kevin Zellweger
 * @Date 29.06.20
 */
@Entity
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

    public void setSet(String set) {
        this.set = set;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setRarity(Rarity rarity) {
        this.rarity = rarity;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setPrice_trend(double price_trend) {
        this.price_trend = price_trend;
    }

    public void setMkmCard(MkmCard mkmCard) {
        this.mkmCard = mkmCard;
    }

    public void setScryfallCard(ScryfallCard scryfallCard) {
        this.scryfallCard = scryfallCard;
    }

    public String getSet() {
        return set;
    }

    public String getTitle() {
        return title;
    }

    public Rarity getRarity() {
        return rarity;
    }

    public int getCount() {
        return count;
    }

    public double getPrice() {
        return price;
    }

    public double getPrice_trend() {
        return price_trend;
    }

    public MkmCard getMkmCard() {
        return mkmCard;
    }

    public ScryfallCard getScryfallCard() {
        return scryfallCard;
    }
}
