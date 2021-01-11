package ch.skaldenmagic.cardmarket.autopricing.domain.mapper.dtos;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

  private int UserId;
  private String userName;
  private LocalDateTime registrationDate;
  private boolean isCommercial;
  private boolean isSeller;
  private String country;
  private int sellCount;
  private int soldItems;

}
