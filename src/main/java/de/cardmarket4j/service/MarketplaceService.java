package de.cardmarket4j.service;

import com.google.gson.JsonElement;
import com.google.gson.JsonSyntaxException;
import de.cardmarket4j.AbstractService;
import de.cardmarket4j.CardMarketService;
import de.cardmarket4j.entity.Article;
import de.cardmarket4j.entity.Expansion;
import de.cardmarket4j.entity.Product;
import de.cardmarket4j.entity.enumeration.HTTPMethod;
import de.cardmarket4j.entity.util.ArticleFilter;
import de.cardmarket4j.entity.util.ProductFilter;
import de.cardmarket4j.util.JsonIO;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * MarketplaceService provides a connection to several marketplace related functions
 *
 * @author QUE
 * @version 30.01.2019
 * @see https://www.mkmapi.eu/ws/documentation/API_2.0:Market_Place_Information
 */
public class MarketplaceService extends AbstractService {

  public MarketplaceService(CardMarketService cardMarket) {
    super(cardMarket);
  }

  /**
   * Returns a List of Articles based on the given productId
   *
   * @param productId
   * @param filter
   * @return {@code List<Article> listArticle}
   * @throws IOException
   * @version 0.7
   * @see https://www.mkmapi.eu/ws/documentation/API_2.0:Articles
   */
  public List<Article> getArticles(int productId) throws IOException {
    List<Article> listArticle = new ArrayList<>();
    JsonElement response = request("articles/" + productId, HTTPMethod.GET);
    for (JsonElement jEle : response.getAsJsonObject().get("article").getAsJsonArray()) {
      listArticle.add(JsonIO.getGson().fromJson(jEle, Article.class));
    }
    return listArticle;
  }

  /**
   * Returns a List of Articles based on the given productId and ArticleFilter
   *
   * @param productId
   * @param filter
   * @return {@code List<Article> listArticle}
   * @throws IOException
   * @version 0.7
   * @see https://www.mkmapi.eu/ws/documentation/API_2.0:Articles
   */
  public List<Article> getArticles(int productId, ArticleFilter filter) throws IOException {
    List<Article> listArticle = new ArrayList<>();
    JsonElement response = request("articles/" + productId + filter.getQuery(), HTTPMethod.GET);
    for (JsonElement jEle : response.getAsJsonObject().get("article").getAsJsonArray()) {
      listArticle.add(JsonIO.getGson().fromJson(jEle, Article.class));
    }
    return listArticle;
  }

  /**
   * Returns a Set of Products based on the expansion ID
   *
   * @param expansionId
   * @return {@code Set<Product> setProduct}
   * @throws IOException
   * @see https://api.cardmarket.com/ws/v2.0/expansions/:idExpansion/singles
   */
  public Set<Product> getExpansionSingles(Integer expansionId) throws IOException {
    Set<Product> setProducts = new HashSet<>();
    JsonElement response = request("expansions/" + expansionId + "/singles", HTTPMethod.GET);
    try {
      if (!response.getAsJsonObject().get("single").isJsonNull()) {
        for (JsonElement jEle : response.getAsJsonObject().get("single").getAsJsonArray()) {
          setProducts.add(JsonIO.getGson().fromJson(jEle, Product.class));
        }
      } else {
        LOGGER.error("Request responded with error: {}", response.getAsString());
      }
    } catch (JsonSyntaxException inEx) {
      LOGGER.error("Error during parsing {}", response.getAsString());
    }
    return setProducts;
  }

  public Set<Expansion> getExpansions(ProductFilter productFilter) throws IOException {
    Set<Expansion> setProducts = new HashSet<>();
    JsonElement response = request("games/1/expansions", HTTPMethod.GET);
    for (JsonElement jEle : response.getAsJsonObject().get("expansion").getAsJsonArray()) {
      setProducts.add(JsonIO.getGson().fromJson(jEle, Expansion.class));
    }
    return setProducts;
  }

  /**
   * Returns a Set of Products based on the given search query. TODO Change Parameter to
   * ProductFilter, similar to ArticleFilter
   *
   * @param searchQuery
   * @return {@code Set<Product> setProduct}
   * @throws IOException
   * @version 0.7
   * @see https://www.mkmapi.eu/ws/documentation/API_2.0:Find_Products
   */
  public Set<Product> getProduct(ProductFilter productFilter) throws IOException {
    Set<Product> setProducts = new HashSet<>();
    JsonElement response = request("products/find?" + productFilter.getQuery(), HTTPMethod.GET);
    for (JsonElement jEle : response.getAsJsonObject().get("product").getAsJsonArray()) {
      setProducts.add(JsonIO.getGson().fromJson(jEle, Product.class));
    }
    return setProducts;
  }

  /**
   * Returns a Product instance from the given productId with full details. This instance contains a
   * PriceGuide
   *
   * @param productId
   * @return {@code Product product}
   * @throws IOException
   * @version 0.7
   * @see https://www.mkmapi.eu/ws/documentation/API_2.0:Product
   */
  public Product getProductDetails(int productId) throws IOException {
    JsonElement response = request("products/" + productId, HTTPMethod.GET);
    return JsonIO.getGson().fromJson(response.getAsJsonObject().get("product"), Product.class);
  }

  public JsonElement getProductsFile() throws IOException {
    return legacyRequest("productlist", HTTPMethod.GET);
  }

  //nicht Nötig, da expansion Id im Productfile angegeben ist.
//	public Set<Product> getProductsOfExpansion(Integer idExpansion) throws IOException {
//		Set<Product> setProducts = new HashSet<>();
//		JsonElement response = request("expansions/"+idExpansion+"/singles", HTTPMethod.GET);
//		for (JsonElement jEle : response.getAsJsonObject().get("single").getAsJsonArray()) {
//			setProducts.add(JsonIO.getGson().fromJson(jEle, Product.class));
//		}
//		return setProducts;
//	}

}
