package ch.softridge.cardmarket.autopricing.model;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Kevin Zellweger
 * @Date 04.07.20
 */
public enum Rarity {
    COMMON("common"),
    UNCOMMON("uncommon"),
    RARE("rare"),
    MYTHIC("mythicrare");

    private final String sorter;

    Rarity(String sorter) {
        this.sorter = sorter;
    }

    public String getSorter(){
        return sorter;
    }

    //****** Reverse Lookup Implementation************//

    //Lookup table
    private static final Map<String, Rarity> lookup = new HashMap<>();

    //Populate the lookup table on loading time
    static
    {
        for(Rarity env : Rarity.values())
        {
            lookup.put(env.getSorter(), env);
        }
    }

    //This method can be used for reverse lookup purpose
    public static Rarity get(String sorter)
    {
        return lookup.get(sorter);
    }

}
