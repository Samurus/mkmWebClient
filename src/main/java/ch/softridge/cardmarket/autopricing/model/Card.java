package ch.softridge.cardmarket.autopricing.model;

import javax.persistence.*;

/**
 * @author Kevin Zellweger
 * @Date 29.06.20
 */
@Entity
@Table(uniqueConstraints={@UniqueConstraint(columnNames={"title","set","condition","isFoil","isSigned","isPlayset"})})
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id; //Unique Id in our own DB

    @Column(name = "set")
    private String set;
    @Column(name = "title")
    private String title;
    @Column(name = "rarity")
    private Rarity rarity;
    @Column(name = "condition")
    private Condition condition;
    @Column(name = "isFoil")
    private boolean isFoil;
    @Column(name = "isSigned")
    private boolean isSigned;
    @Column(name = "isPlayset")
    private boolean isPlayset;
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

    public Card(String[] sorterResult){
        this.title = sorterResult[2];
        this.set = sorterResult[0];
        this.rarity = parseRarity(sorterResult[1]);
        this.count = parseCount(sorterResult[3]);
        this.price = parsePrice(sorterResult[4]);
        this.price_trend = parsePrice_trend(sorterResult[5]);
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

    private Rarity parseRarity(String rarity) {
        return Rarity.get(rarity);
    }

    public void setCount(int count) {
        this.count = count;
    }

    private Integer parseCount(String count){
        return Integer.valueOf(count);
    }

    public void setPrice(double price) {
        this.price = price;
    }

    private Double parsePrice(String price){
        return Double.valueOf(price);
    }

    public void setPrice_trend(double price_trend) {
        this.price_trend = price_trend;
    }

    private Double parsePrice_trend(String price_trend){
        return Double.valueOf(price_trend);
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
