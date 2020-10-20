package de.cardmarket4j.entity;

import com.neovisionaries.i18n.LanguageCode;
import de.cardmarket4j.entity.enumeration.Condition;

import java.math.BigDecimal;

public interface CardMarketArticle {
	public Integer getArticleId();

	public String getComment();

	public Condition getCondition();

	public LanguageCode getLanguageCode();

	public BigDecimal getPrice();

	public Integer getProductId();

	public int getQuantity();

	public boolean isAltered();

	public boolean isFirstEdition();

	public boolean isFoil();

	public boolean isPlayset();

	public boolean isSigned();
}
