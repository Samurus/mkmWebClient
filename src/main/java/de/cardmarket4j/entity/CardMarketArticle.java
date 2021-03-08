package de.cardmarket4j.entity;

import com.neovisionaries.i18n.LanguageCode;
import de.cardmarket4j.entity.enumeration.Condition;
import java.math.BigDecimal;

public interface CardMarketArticle {

  Integer getArticleId();

  String getComment();

  Condition getCondition();

  LanguageCode getLanguageCode();

  BigDecimal getPrice();

  Integer getProductId();

  int getQuantity();

  boolean isAltered();

  boolean isFirstEdition();

  boolean isFoil();

  boolean isPlayset();

  boolean isSigned();
}
