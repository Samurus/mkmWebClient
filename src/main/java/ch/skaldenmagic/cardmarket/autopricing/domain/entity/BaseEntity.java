package ch.skaldenmagic.cardmarket.autopricing.domain.entity;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@MappedSuperclass
@NoArgsConstructor
public abstract class BaseEntity implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Version
  private Long version;

  @Override
  public int hashCode() {
    return Objects.hash(id, version);
  }

  @Override
  public String toString() {
    return "BaseEntity{" +
        "id=" + id +
        ", version=" + version +
        '}';
  }
}