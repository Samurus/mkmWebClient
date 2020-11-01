package ch.softridge.cardmarket.autopricing.domain.entity;

import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Kevin Zellweger
 * @Date 28.10.20
 */
@Setter
@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "reprint")
public class ProductReprintEntity extends BaseEntity {

  int productId;
}
