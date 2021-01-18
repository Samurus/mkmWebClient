package ch.skaldenmagic.cardmarket.autopricing.domain.mapper.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Kevin Zellweger
 * @Date 12.01.21
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class LocalizationDto {

  private String language;
  private String productName;
}
