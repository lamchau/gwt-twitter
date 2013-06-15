package com.lchau.gwt.twitter.shared.dto;

import java.util.Date;

import org.junit.Assert;
import org.junit.Test;

import com.lchau.gwt.twitter.shared.dto.Tweet.TweetComparator;

/**
 * Native JUnit test for {@link Tweet}.
 *
 * @author Lam Chau
 */
public class TweetTest {
  private static final String EMPTY_STRING = "";

  @Test(expected = IllegalArgumentException.class)
  public void testNullDate() {
    new Tweet(null, EMPTY_STRING, EMPTY_STRING);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNullMessage() {
    new Tweet(new Date(), EMPTY_STRING, null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNullUserName() {
    new Tweet(new Date(), null, EMPTY_STRING);
  }

  @Test
  public void testTweetEquality() {
    Date date = new Date();
    String user = "user";
    String message = "Hello World";
    Tweet t1 = new Tweet(date, user, message);
    Tweet t2 = new Tweet(date, user, message);
    Assert.assertEquals(t1, t2);
  }

  @Test
  public void testTweetNullAfter() {
    Date date = new Date();
    String user = "user";
    String message = "Hello World";
    Tweet t1 = new Tweet(date, user, message);
    Assert.assertEquals(Tweet.AFTER, TweetComparator.DATE_DESC.compare(t1, null));
  }

  @Test
  public void testTweetNullBefore() {
    Date date = new Date();
    String user = "user";
    String message = "Hello World";
    Tweet t1 = new Tweet(date, user, message);
    Assert.assertEquals(Tweet.BEFORE, TweetComparator.DATE_DESC.compare(null, t1));
  }
}
