package ch.softridge.cardmarket.autopricing.domain.service;

import ch.softridge.cardmarket.autopricing.domain.mapper.AccountMapper;
import ch.softridge.cardmarket.autopricing.domain.mapper.dtos.AccountDto;
import ch.softridge.cardmarket.autopricing.domain.service.exceptions.MkmAPIException;
import de.cardmarket4j.entity.Account;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service layer for current Mkm Account
 *
 * @author Kevin Zellweger
 * @Date 01.11.20
 */
@Service
public class AccountService {

  @Autowired
  private AccountMapper accountMapper;

  @Autowired
  private MkmService mkmService;

  public AccountDto getAccount() {
    try {
      Account account = mkmService.getCardMarket().getAccountService().getAccount();
      AccountDto dto = accountMapper.accountToDto(account);
      return dto;
    } catch (IOException e) {
      throw new MkmAPIException(de.cardmarket4j.service.AccountService.class, "getAccount()");
    }
  }
}
