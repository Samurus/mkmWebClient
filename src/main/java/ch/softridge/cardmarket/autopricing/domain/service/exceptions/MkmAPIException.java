package ch.softridge.cardmarket.autopricing.domain.service.exceptions;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;
import org.springframework.util.StringUtils;

/**
 * @author Kevin Zellweger
 * @Date 01.11.20
 */
public class MkmAPIException extends RuntimeException {

  public MkmAPIException(Class clazz, String... methodCall) {
    super(MkmAPIException.generateMessage(clazz.getSimpleName(),
        toMap(String.class, String.class, methodCall)));
  }

  private static String generateMessage(String entity, Map<String, String> methodCall) {
    return StringUtils.capitalize(entity) +
        " throwed an Error druing  " +
        methodCall;
  }

  private static <K, V> Map<K, V> toMap(
      Class<K> keyType, Class<V> valueType, Object... entries) {
    if (entries.length % 2 == 1) {
      throw new IllegalArgumentException("Invalid entries");
    }
    return IntStream.range(0, entries.length / 2).map(i -> i * 2)
        .collect(HashMap::new,
            (m, i) -> m.put(keyType.cast(entries[i]), valueType.cast(entries[i + 1])),
            Map::putAll);
  }

}
