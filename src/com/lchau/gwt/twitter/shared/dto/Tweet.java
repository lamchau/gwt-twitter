package com.lchau.gwt.twitter.shared.dto;

import java.util.Comparator;
import java.util.Date;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * DTO representing a message or a "tweet".
 *
 * @author Lam Chau
 */
public final class Tweet implements Comparable<Tweet>, IsSerializable {
  /**
   * Comparators for all {@link Tweet tweets} used for sorting.<br />
   * <br />
   * Note: We're use an enum to leverage the "free" serialization provided by GWT-RPC and
   * primitives, while also reducing clutter in the namespace for later implementation.
   *
   */
  public enum TweetComparator implements Comparator<Tweet> {
    DATE_DESC() {
      @Override
      public int compare(Tweet o1, Tweet o2) {
        if (o1 == null && o2 == null) {
          return EQUALS;
        } else if (o1 == null) {
          return BEFORE;
        } else if (o2 == null) {
          return AFTER;
        }
        final Date d1 = o1.creationDate;
        final Date d2 = o2.creationDate;
        return -d1.compareTo(d2);
      }
    };

    @Override
    public abstract int compare(Tweet o1, Tweet o2);
  }

  // package protected for testing
  static final int EQUALS = 0;
  static final int BEFORE = -1;
  static final int AFTER = 1;

  /**
   * Runtime check for <code>null</code> objects. Gives context to {@link NullPointerException}s
   * (useful for debugging) -- ensures contract during development.
   *
   * @param o the object to check
   * @param descriptiveName the description/name of the object
   */
  private static final void assertNotNull(Object o, String descriptiveName) {
    if (o == null) {
      throw new IllegalArgumentException(descriptiveName + " cannot be null");
    }
  }

  private Date creationDate;
  private String user;
  private String message;

  public Tweet() {
  }

  /**
   * Create a tweet object.
   *
   * @param creationDate the tweet creation date
   * @param user the authenticated user
   * @param message the message
   * @throws IllegalArgumentException if any of the arguments are <code>null</code>
   */
  public Tweet(Date creationDate, String user, String message) {
    assertNotNull(creationDate, "Date");
    assertNotNull(user, "User");
    assertNotNull(message, "Message");
    this.creationDate = creationDate;
    this.user = user;
    this.message = message;
  }

  @Override
  public int compareTo(Tweet o) {
    return TweetComparator.DATE_DESC.compare(this, o);
  }

  @Override
  public boolean equals(Object o) {
    if (!(o instanceof Tweet)) {
      return false;
    }
    final Tweet t1 = this;
    final Tweet t2 = (Tweet) o;
    final Date d1 = t1.creationDate;
    final Date d2 = t2.creationDate;
    final String u1 = t1.user;
    final String u2 = t2.user;
    final String m1 = t1.message;
    final String m2 = t2.message;
    return d1.equals(d2) && u1.equals(u2) && m1.equals(m2);
  }

  /**
   * Gets the creation date.
   *
   * @return the creation date
   */
  public Date getCreationDate() {
    return creationDate;
  }

  /**
   * Gets the message.
   *
   * @return the message
   */
  public String getMessage() {
    return message;
  }

  /**
   * Gets the current user name.
   *
   * @return the username
   */
  public String getUser() {
    return user;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((creationDate == null) ? 0 : creationDate.hashCode());
    result = prime * result + ((message == null) ? 0 : message.hashCode());
    result = prime * result + ((user == null) ? 0 : user.hashCode());
    return result;
  }

  @Override
  public String toString() {
    return creationDate.toString() + " @" + user + " " + message;
  }
}
