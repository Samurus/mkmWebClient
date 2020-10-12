package ch.softridge.cardmarket.autopricing.model;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Kevin Zellweger
 * @Date 30.07.20
 */
public enum Condition {
    MINT("M"),
    NEARMINT("NM"),
    EXCELLENT("EX"),
    GOOD("GD"),
    LIGHTPLAYED("LP"),
    PLAYED("PL"),
    POOR("");

    private final String sorter;

    Condition(String sorter) {
        this.sorter = sorter;
    }

    public String getSorter(){
        return sorter;
    }

    //****** Reverse Lookup Implementation************//

    //Lookup table
    private static final Map<String, Condition> lookup = new HashMap<>();

    //Populate the lookup table on loading time
    static
    {
        for(Condition env : Condition.values())
        {
            lookup.put(env.getSorter(), env);
        }
    }

    //This method can be used for reverse lookup purpose
    public static Condition get(String sorter)
    {
        return lookup.get(sorter);
    }

}
