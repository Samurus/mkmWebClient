package ch.skaldenmagic.cardmarket.autopricing.domain.model;

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

  //Lookup table
  private static final Map<String, Rarity> lookup = new HashMap<>();

  //Populate the lookup table on loading time
  static {
    for (Rarity env : Rarity.values()) {
      lookup.put(env.getSorter(), env);
    }
  }

  private final String sorter;

  //****** Reverse Lookup Implementation************//

  Rarity(String sorter) {
    this.sorter = sorter;
  }

  //This method can be used for reverse lookup purpose
  public static Rarity get(String sorter) {
    return lookup.get(sorter);
  }

  public String getSorter() {
    return sorter;
  }

}
