package ch.softridge.cardmarket.autopricing.domain.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
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
public class ScryfallCard {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id; //Unique Id in our own DB
  @Column(unique = true)
  private String scryfallId;

  @OneToOne(mappedBy = "scryfallCard")
  private Card card;

  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(unique = true)
  private Prices prices;

  public ScryfallCard(String scryfallId, Card card) {
    this.scryfallId = scryfallId;
    this.card = card;
  }

  protected ScryfallCard() {
  }
}
