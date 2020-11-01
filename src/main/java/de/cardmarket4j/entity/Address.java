package de.cardmarket4j.entity;

import com.neovisionaries.i18n.CountryCode;

/**
 * @author QUE
 * @see https://www.mkmapi.eu/ws/documentation/API_2.0:Entities:Address
 */
public class Address {

  private String name;
  private String extra;
  private String street;
  private String zip;
  private String city;
  private CountryCode country;

  public Address(String name, String extra, String street, String zip, String city,
      CountryCode country) {
    this.name = name;
    this.extra = extra;
    this.street = street;
    this.zip = zip;
    this.city = city;
    this.country = country;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    Address other = (Address) obj;
    if (city == null) {
      if (other.city != null) {
        return false;
      }
    } else if (!city.equals(other.city)) {
      return false;
    }
    if (country != other.country) {
      return false;
    }
    if (extra == null) {
      if (other.extra != null) {
        return false;
      }
    } else if (!extra.equals(other.extra)) {
      return false;
    }
    if (name == null) {
      if (other.name != null) {
        return false;
      }
    } else if (!name.equals(other.name)) {
      return false;
    }
    if (street == null) {
      if (other.street != null) {
        return false;
      }
    } else if (!street.equals(other.street)) {
      return false;
    }
    if (zip == null) {
      return other.zip == null;
    } else {
      return zip.equals(other.zip);
    }
  }

  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public CountryCode getCountry() {
    return country;
  }

  public void setCountry(CountryCode country) {
    this.country = country;
  }

  public String getExtra() {
    return extra;
  }

  public void setExtra(String extra) {
    this.extra = extra;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getStreet() {
    return street;
  }

  public void setStreet(String street) {
    this.street = street;
  }

  public String getZip() {
    return zip;
  }

  public void setZip(String zip) {
    this.zip = zip;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((city == null) ? 0 : city.hashCode());
    result = prime * result + ((country == null) ? 0 : country.hashCode());
    result = prime * result + ((extra == null) ? 0 : extra.hashCode());
    result = prime * result + ((name == null) ? 0 : name.hashCode());
    result = prime * result + ((street == null) ? 0 : street.hashCode());
    result = prime * result + ((zip == null) ? 0 : zip.hashCode());
    return result;
  }

  @Override
  public String toString() {
    return "Address [" + (name != null ? "name=" + name + ", " : "")
        + (extra != null ? "extra=" + extra + ", " : "") + (street != null ? "street=" + street
        + ", " : "")
        + (zip != null ? "zip=" + zip + ", " : "") + (city != null ? "city=" + city + ", " : "")
        + (country != null ? "country=" + country : "") + "]";
  }
}
