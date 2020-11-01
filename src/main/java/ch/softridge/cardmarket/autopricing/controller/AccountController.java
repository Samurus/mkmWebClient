package ch.softridge.cardmarket.autopricing.controller;

import ch.softridge.cardmarket.autopricing.domain.mapper.dtos.AccountDto;
import ch.softridge.cardmarket.autopricing.domain.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Kevin Zellweger
 * @Date 01.11.20
 */

@RestController
@RequestMapping(value = "/account", produces = MediaType.APPLICATION_JSON_VALUE)
public class AccountController {

  @Autowired
  private AccountService accountService;

  @GetMapping("/")
  public AccountDto getAccount() {
    return accountService.getAccount();
  }
}
