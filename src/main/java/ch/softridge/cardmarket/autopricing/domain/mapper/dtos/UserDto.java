package ch.softridge.cardmarket.autopricing.domain.mapper.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

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
