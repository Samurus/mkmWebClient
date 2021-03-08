package ch.skaldenmagic.cardmarket.autopricing.domain.service.exceptions;

import org.springframework.util.StringUtils;

/**
 * @author Kevin Zellweger
 * @Date 01.11.20
 */
public class MkmAPIException extends RuntimeException {

  public MkmAPIException(Class clazz, String... details) {
    super(MkmAPIException.generateMessage(clazz.getSimpleName(),
        details));
  }

  private static String generateMessage(String clazz, String[] details) {
    StringBuilder strb = new StringBuilder();
    for (String detail : details) {
      strb.append(detail);
      strb.append("\n");
    }
    return StringUtils.capitalize(clazz) +
        "Details :" +
        strb.toString();
  }

}
