package de.cardmarket4j.entity;

import java.util.List;

/**
 * @author QUE
 * @see https://www.mkmapi.eu/ws/documentation/API_2.0:Account_Messages
 */
public class Conversation {

  private User partner;
  private List<Message> listMessages;
  private int unreadMessages;

  public Conversation(User partner, List<Message> listMessages, int unreadMessages) {
    this.partner = partner;
    this.listMessages = listMessages;
    this.unreadMessages = unreadMessages;
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
    Conversation other = (Conversation) obj;
    if (listMessages == null) {
      if (other.listMessages != null) {
        return false;
      }
    } else if (!listMessages.equals(other.listMessages)) {
      return false;
    }
    if (partner == null) {
      if (other.partner != null) {
        return false;
      }
    } else if (!partner.equals(other.partner)) {
      return false;
    }
    return unreadMessages == other.unreadMessages;
  }

  public List<Message> getListMessages() {
    return listMessages;
  }

  public void setListMessages(List<Message> listMessages) {
    this.listMessages = listMessages;
  }

  public User getPartner() {
    return partner;
  }

  public void setPartner(User partner) {
    this.partner = partner;
  }

  public int getUnreadMessages() {
    return unreadMessages;
  }

  public void setUnreadMessages(int unreadMessages) {
    this.unreadMessages = unreadMessages;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((listMessages == null) ? 0 : listMessages.hashCode());
    result = prime * result + ((partner == null) ? 0 : partner.hashCode());
    result = prime * result + unreadMessages;
    return result;
  }

  @Override
  public String toString() {
    return "Conversation [" + (partner != null ? "partner=" + partner + ", " : "")
        + (listMessages != null ? "listMessages=" + listMessages + ", " : "") + "unreadMessages="
        + unreadMessages + "]";
  }
}
