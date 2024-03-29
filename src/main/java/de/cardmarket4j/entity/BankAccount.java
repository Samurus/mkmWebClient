package de.cardmarket4j.entity;

/**
 * @author QUE
 * @see https://www.mkmapi.eu/ws/documentation/API_2.0:Entities:Account
 */
public class BankAccount {

  private String registeredOwner;
  private String iban;
  private String bic;
  private String bankName;

  public BankAccount(String registeredOwner, String iban, String bic, String bankName) {
    this.registeredOwner = registeredOwner;
    this.iban = iban;
    this.bic = bic;
    this.bankName = bankName;
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
    BankAccount other = (BankAccount) obj;
    if (bankName == null) {
      if (other.bankName != null) {
        return false;
      }
    } else if (!bankName.equals(other.bankName)) {
      return false;
    }
    if (bic == null) {
      if (other.bic != null) {
        return false;
      }
    } else if (!bic.equals(other.bic)) {
      return false;
    }
    if (iban == null) {
      if (other.iban != null) {
        return false;
      }
    } else if (!iban.equals(other.iban)) {
      return false;
    }
    if (registeredOwner == null) {
      return other.registeredOwner == null;
    } else
      return registeredOwner.equals(other.registeredOwner);
  }

  public String getBankName() {
    return bankName;
  }

  public void setBankName(String bankName) {
    this.bankName = bankName;
  }

  public String getBic() {
    return bic;
  }

  public void setBic(String bic) {
    this.bic = bic;
  }

  public String getIban() {
    return iban;
  }

  public void setIban(String iban) {
    this.iban = iban;
  }

  public String getRegisteredOwner() {
    return registeredOwner;
  }

  public void setRegisteredOwner(String registeredOwner) {
    this.registeredOwner = registeredOwner;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((bankName == null) ? 0 : bankName.hashCode());
    result = prime * result + ((bic == null) ? 0 : bic.hashCode());
    result = prime * result + ((iban == null) ? 0 : iban.hashCode());
    result = prime * result + ((registeredOwner == null) ? 0 : registeredOwner.hashCode());
    return result;
  }

  @Override
  public String toString() {
    return "BankAccount [" + (registeredOwner != null ? "registeredOwner=" + registeredOwner + ", "
        : "")
        + (iban != null ? "iban=" + iban + ", " : "") + (bic != null ? "bic=" + bic + ", " : "")
        + (bankName != null ? "bankName=" + bankName : "") + "]";
  }
}
