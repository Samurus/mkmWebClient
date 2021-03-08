package de.cardmarket4j.entity;

import java.time.LocalDateTime;

/**
 * @author QUE
 * @see https://www.mkmapi.eu/ws/documentation/API_2.0:Account_Messages
 */
public class Message {

  private int messageId;
  private boolean sending;
  private boolean unread;
  private LocalDateTime date;
  private String text;

  public Message(int messageId, boolean sending, boolean unread, LocalDateTime date, String text) {
    this.messageId = messageId;
    this.sending = sending;
    this.unread = unread;
    this.date = date;
    this.text = text;
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
    Message other = (Message) obj;
    if (date == null) {
      if (other.date != null) {
        return false;
      }
    } else if (!date.equals(other.date)) {
      return false;
    }
    if (messageId != other.messageId) {
      return false;
    }
    if (sending != other.sending) {
      return false;
    }
    if (text == null) {
      if (other.text != null) {
        return false;
      }
    } else if (!text.equals(other.text)) {
      return false;
    }
    return unread == other.unread;
  }

  public LocalDateTime getDate() {
    return date;
  }

  public void setDate(LocalDateTime date) {
    this.date = date;
  }

  public int getMessageId() {
    return messageId;
  }

  public void setMessageId(int messageId) {
    this.messageId = messageId;
  }

  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((date == null) ? 0 : date.hashCode());
    result = prime * result + messageId;
    result = prime * result + (sending ? 1231 : 1237);
    result = prime * result + ((text == null) ? 0 : text.hashCode());
    result = prime * result + (unread ? 1231 : 1237);
    return result;
  }

  public boolean isSending() {
    return sending;
  }

  public void setSending(boolean sending) {
    this.sending = sending;
  }

  public boolean isUnread() {
    return unread;
  }

  public void setUnread(boolean unread) {
    this.unread = unread;
  }

  @Override
  public String toString() {
    return "Message [messageId=" + messageId + ", sending=" + sending + ", unread=" + unread + ", "
        + (date != null ? "date=" + date + ", " : "") + (text != null ? "text=" + text : "") + "]";
  }

}
