package ch.softridge.cardmarket.autopricing.model;

/**
 * @author Kevin Zellweger
 * @Date 29.06.20
 */
public class Card {
//set,rarity,title,count,price,price_trend
    private String set;
    private String rarity;
    private String title;
    private int count;
    private double price;
    private double price_trend;
    private int language = 1; //TODO make enum and on Setter
    private int gameId = 1; //TODO make enum and on Setter


    protected Card(){}

    public Card(String set, String rarity, String title, int count, double price, double price_trend) {
        this.set = set;
        this.rarity = rarity;
        this.title = title;
        this.count = count;
        this.price = price;
        this.price_trend = price_trend;
    }

    public String getSet() {
        return set;
    }

    public void setSet(String set) {
        this.set = set;
    }

    public String getRarity() {
        return rarity;
    }

    public void setRarity(String rarity) {
        this.rarity = rarity;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getPrice_trend() {
        return price_trend;
    }

    public void setPrice_trend(double price_trend) {
        this.price_trend = price_trend;
    }

    public int getCount() {
        return count;
    }

    public int getLanguage() {
        return language;
    }

    public void setLanguage(int language) {
        this.language = language;
    }

    public int getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
