package ch.softridge.cardmarket.autopricing.model;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.domain.EntityScan;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Kevin Zellweger
 * @Date 29.06.20
 */
@Entity
@Setter @Getter @Slf4j
public class Card {
//set,rarity,title,count,price,price_trend
    @Id
    @GeneratedValue
    private long id; //Unique Id in our owen DB
    //Start Properties given from the Results.csv from the Sorter-Device
    private String set;
    private String rarity;
    private String title;
    private int count;
    private double price;
    private double price_trend;

    //Start Properties from MKM-API
    private long mkmProductId;
    private long mkmMetaProductId;
    private int mkmCountReprints;
    private String enName;

    //TODO: Consider to construct smart Database Schema, normalized


    /**

     Example what we can expect:

     {
     product: {
     idProduct: 265882
     idMetaproduct: 209344
     countReprints: 1
     enName: "Shrike Harpy"
     localization: [
     {
     idLanguage: 1
     languageName: "English"
     productName: "Shrike Harpy"
     }
     {
     idLanguage: 2
     languageName: "French"
     productName: "Harpie grièche"
     }
     ]
     website: "/Products/Singles/Born+of+the+Gods/Shrike+Harpy"
     image: "./img/cards/Born_of_the_Gods/shrike_harpy.jpg"
     gameName: "Magic the Gathering"
     categoryName: "Magic Single"
     number: "83"
     rarity: "Uncommon"
     expansionName: "Born of the Gods"
     links: {}
     }
     }



     Product Entity from MKM API:

     product: {
        idProduct:                  // Product ID
        idMetaproduct:              // Metaproduct ID
        countReprints:              // Number of total products bundled by the metaproduct
        enName:                     // Product's English name
        localization: {}            // localization entities for the product's name
        category: {                 // Category entity the product belongs to
            idCategory:             // Category ID
            categoryName:           // Category's name
        }
        website:                    // URL to the product (relative to MKM's base URL)
        image:                      // Path to the product's image
        gameName:                   // the game's name
        categoryName:               // the category's name
        number:                     // Number of product within the expansion (where applicable)
        rarity:                     // Rarity of product (where applicable)
        expansionName:              // Expansion's name
        links: {}                   // HATEOAS links
         The following information is only returned for responses that return the detailed product entity
    expansion: {                // detailed expansion information (where applicable)
        idExpansion:
        enName:
        expansionIcon:
    }
    priceGuide: {               // Price guide entity '''(ATTN: not returned for expansion requests)'''
        SELL:                   // Average price of articles ever sold of this product
        LOW:                    // Current lowest non-foil price (all conditions)
        LOWEX+:                 // Current lowest non-foil price (condition EX and better)
        LOWFOIL:                // Current lowest foil price
        AVG:                    // Current average non-foil price of all available articles of this product
        TREND:                  // Current trend price
        TRENDFOIL:              // Current foil trend price
    }
    reprint: [                  // Reprint entities for each similar product bundled by the metaproduct
    {
        idProduct:
        expansion:
        expIcon:
    }
        ]
}



     Example Full Details:
     {
     product: {
     idProduct: 265535
     idMetaproduct: 9372
     countReprints: 2
     enName: "Springleaf Drum"
     localization: [
     {
     idLanguage: 1
     languageName: "English"
     productName: "Springleaf Drum"
     }
     {
     idLanguage: 2
     languageName: "French"
     productName: "Tambour de sautefeuille"
     }
     ]
     website: "/Products/Singles/Born+of+the+Gods/Springleaf+Drum"
     image: "./img/cards/Born_of_the_Gods/springleaf_drum.jpg"
     gameName: "Magic the Gathering"
     categoryName: "Magic Single"
     number: "162"
     rarity: "Uncommon"
     expansion: {
     idExpansion: 1469
     enName: "Born of the Gods"
     expansionIcon: 246
     }
     priceGuide: {
     SELL: 0.29
     LOW: 0.02
     LOWEX: 0
     LOWFOIL: 0.02
     AVG: 0.44
     TREND: 0.28
     TRENDFOIL: 4.00
     }
     reprint: [
     {
     idProduct: 18002
     expansion: "Lorwyn"
     expansionIcon: 81
     }
     ]
     links: {}
     }
     }


    **/



    //Custom defined Properties
    private Language language;
    private Game game;


    protected Card(){}

}
