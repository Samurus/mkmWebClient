package ch.skaldenmagic.cardmarket.autopricing.domain.mapper;

import ch.skaldenmagic.cardmarket.autopricing.domain.mapper.dtos.AccountDto;
import de.cardmarket4j.entity.Account;
import org.mapstruct.Mapper;

/**
 * @author Kevin Zellweger
 * @Date 01.11.20
 */
@Mapper(componentModel = "spring")
public interface AccountMapper {

  AccountDto mkmToDto(Account account);

}
