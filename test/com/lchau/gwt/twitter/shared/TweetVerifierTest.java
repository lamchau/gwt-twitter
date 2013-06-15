package com.lchau.gwt.twitter.shared;

import java.util.Date;

import junit.framework.Assert;

import org.junit.Test;

public class TweetVerifierTest {
  private static final String MAX_MESSAGE =
      "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Aliquam non erat quis arcu tempus aliquam ut eget urna. Lorem ipsum dolor sit amet.";

  @Test
  public void testIsValidCreationDateFuture() {
    Date future = new Date(new Date().getTime() + 5000);
    Assert.assertFalse(TweetVerifier.isValidCreationDate(future));
  }

  @Test
  public void testIsValidCreationDatePast() {
    Date past = new Date(new Date().getTime() - 5000);
    Assert.assertTrue(TweetVerifier.isValidCreationDate(past));
  }

  @Test
  public void testIsValidMessage() {
    Assert.assertTrue(TweetVerifier.isValidMessage(MAX_MESSAGE));
  }

  @Test
  public void testIsValidMessageEmpty() {
    Assert.assertFalse(TweetVerifier.isValidMessage(TweetVerifier.EMPTY_STRING));
  }

  @Test
  public void testIsValidMessageNull() {
    Assert.assertFalse(TweetVerifier.isValidMessage(null));
  }

  @Test
  public void testIsValidMessageTooLong() {
    Assert.assertFalse(TweetVerifier.isValidMessage(MAX_MESSAGE + "."));
  }

  @Test
  public void testIsValidUsername() {
    Assert.assertTrue(TweetVerifier.isValidUsername("username"));
  }

  @Test
  public void testIsValidUsernameEmpty() {
    Assert.assertFalse(TweetVerifier.isValidUsername(TweetVerifier.EMPTY_STRING));
  }

  @Test
  public void testIsValidUsernameNull() {
    Assert.assertFalse(TweetVerifier.isValidUsername(null));
  }

  @Test
  public void testIsValidUsernameTooLong() {
    Assert.assertFalse(TweetVerifier.isValidUsername("012345678901234567891"));
  }
}
