package ch.softridge.cardmarket.autopricing.domain.mapper.dtos;

import com.neovisionaries.i18n.CountryCode;
import de.cardmarket4j.entity.Address;
import de.cardmarket4j.entity.enumeration.DeliverySpeed;
import de.cardmarket4j.entity.enumeration.Reputation;
import de.cardmarket4j.entity.enumeration.RiskGroup;
import de.cardmarket4j.entity.enumeration.SellerActivationStatus;
import de.cardmarket4j.entity.enumeration.UserType;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Kevin Zellweger
 * @Date 01.11.20
 */

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AccountDto {

  private int userId;
  private String userName;
  private LocalDateTime registrationDate;
  private UserType userType;
  private boolean seller;
  private String companyName;
  private String firstName;
  private String lastName;
  private Address address;
  private String vat;
  private RiskGroup riskGroup;
  private Reputation reputation;
  private int expectedDeliveryTime;
  private int amountSales;
  private int amountSoldItems;
  private int averageShippingTime;
  private boolean onVacation;

  private CountryCode country;
  private boolean maySell;
  private SellerActivationStatus sellerActivationStatus;
  private DeliverySpeed deliverySpeed;
  private boolean activated;
  private BigDecimal totalBalance;
  private BigDecimal moneyBalance;
  private BigDecimal bonusBalance;
  private BigDecimal unpaidAmount;
  private BigDecimal providerRechargeAmount;
  private int amountItemsInShoppingCard;
  private int amountUnreadMessages;

}
