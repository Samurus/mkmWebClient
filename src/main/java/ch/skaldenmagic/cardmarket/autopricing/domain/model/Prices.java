package ch.skaldenmagic.cardmarket.autopricing.domain.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Kevin Zellweger
 * @Date 04.07.20
 */
@Entity
@Getter
@Setter
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

  protected Prices() {
  }
}
