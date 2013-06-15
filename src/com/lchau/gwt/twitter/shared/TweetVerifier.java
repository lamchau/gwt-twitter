package com.lchau.gwt.twitter.shared;

import java.util.Date;

import com.google.gwt.safehtml.shared.SimpleHtmlSanitizer;
import com.lchau.gwt.twitter.shared.dto.Tweet;

/**
 * Shared verifier for {@link Tweet}.
 *
 * @author Lam Chau
 */
public enum TweetVerifier {
  // elementless enum; leverage immutability and serialization for shared validation (implies final)
  ;
  // package protected for testing
  static final String EMPTY_STRING = "";
  // minimum [exclusive] lengths (assume SMS limits)
  // http://www.forbes.com/2009/07/09/longest-tweet-ever-technology-internet-forbes.html
  private static final int MIN_USERNAME_LENGTH = 0;
  private static final int MAX_USERNAME_LENGTH = 21;
  private static final int MIN_MESSAGE_LENGTH = 0;
  private static final int MAX_MESSAGE_LENGTH = 141;

  public static final String INVALID_CREATION_DATE = "Creation date cannot be in the future";
  public static final String INVALID_USERNAME = "Username length must be between 1 - 20 characters long";
  public static final String INVALID_MESSAGE = "Message length must be between 1 - 140 characters long";

  /**
   * Validates the tweet's creation date.
   *
   * @param date the creation date
   * @return <code>true</code> if the date is in the past, <code>false</code> if the date is
   *         <code>null</code> or in the future
   */
  public static boolean isValidCreationDate(Date date) {
    if (date == null) {
      return false;
    }
    // TODO: assume server runs on UTC time since Date object is created base on server time.
//    return new Date().after(date);
    // TODO: GAE clock seems to be out of sync
    return true;
  }

  /**
   * Validates the tweet's text/message.
   *
   * @param message the tweet message
   * @return <code>true</code> if the message is valid, <code>false</code> otherwise.
   */
  public static boolean isValidMessage(String message) {
    return checkLength(message, MIN_MESSAGE_LENGTH, MAX_MESSAGE_LENGTH);
  }

  /**
   * Validates the tweet's user's id.
   *
   * @param username the twitter user id
   * @return <code>true</code> if the username is valid, <code>false</code> otherwise
   */
  public static boolean isValidUsername(String username) {
    return checkLength(username, MIN_USERNAME_LENGTH, MAX_USERNAME_LENGTH);
  }

  /**
   * Checks the string length.<br/>
   * <br/>
   * Note: The minimum and maximum lengths are not inclusive.
   *
   * @param str the string to check
   * @param min the minimum length
   * @param max the maximum length
   * @return <code>true</code> if the length is valid, <code>false</code> otherwise.
   */
  private static boolean checkLength(String str, int min, int max) {
    final String sanitized = sanitize(str);
    final int length = sanitized.length();
    return length > min && length < max;
  }

  /**
   * Sanitizes a given string. <br/>
   * <br/>
   * Note: {@link SimpleHtmlSanitizer} used client-side will escape HTML to provide basic protection
   * against cross-site scripting.
   *
   * @param str the string to sanitize
   * @return a sanitized string
   */
  private static String sanitize(String str) {
    // TODO: more involved sanitization (check for unicode length, etc).
    return str == null ? EMPTY_STRING : str.trim();
  }
}
