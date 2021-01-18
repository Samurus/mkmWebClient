package ch.skaldenmagic.cardmarket.autopricing.controller.error;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Wrapper Class for occurring Errors. Can be used to provide meaningful error messages to the
 * User.
 *
 * @author Kevin Zellweger
 * @Date 01.11.20
 */
@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
class ApiValidationError implements ApiSubError {

  private String object;
  private String field;
  private Object rejectedValue;
  private String message;

  public ApiValidationError(String object, String message) {
    this.object = object;
    this.message = message;
  }
}
