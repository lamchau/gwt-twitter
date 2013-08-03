package com.lchau.gwt.twitter.server;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Random;

import com.google.gwt.safehtml.shared.SimpleHtmlSanitizer;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.lchau.gwt.twitter.client.TwitterService;
import com.lchau.gwt.twitter.shared.TweetVerifier;
import com.lchau.gwt.twitter.shared.dto.Tweet;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class TwitterServiceImpl extends RemoteServiceServlet implements TwitterService {
  private static final int MAX_NUMBER_OF_TWEETS = 20;

  // PriorityQueue would not serialize -- had to switch to a LinkedList
  private List<Tweet> list = new ArrayList<Tweet>();
  private final Random random = new SecureRandom();

  public TwitterServiceImpl() {
    generateTweets();
  }

  @Override
  public void clearTweets() {
    list.clear();
  }

  @Override
  public String createRandomMessage() {
    return ObliqueStrategies.nextMessage();
  }

  @Override
  public List<Tweet> getMostRecentTweets() {
    // deep copy of most recent tweets; retaining all tweets
    final List<Tweet> result = new ArrayList<Tweet>(MAX_NUMBER_OF_TWEETS);
    final int maxTweets =
        list.size() >= MAX_NUMBER_OF_TWEETS ? list.size() - MAX_NUMBER_OF_TWEETS : 0;
    // we only append new tweets, so reverse chronological tweets are simply reversed
    for (int i = list.size() - 1; i >= maxTweets; i--) {
      result.add(list.get(i));
    }
    return result;
  }

  @Override
  public synchronized void sendMessage(Tweet tweet) throws IllegalArgumentException {
    if (tweet == null) {
      return;
    }
    // server-side validation
    if (!TweetVerifier.isValidUsername(tweet.getUser())) {
      throw new IllegalArgumentException(TweetVerifier.INVALID_USERNAME);
    }
    if (!TweetVerifier.isValidCreationDate(tweet.getCreationDate())) {
      throw new IllegalArgumentException(TweetVerifier.INVALID_CREATION_DATE);
    }
    if (!TweetVerifier.isValidMessage(tweet.getMessage())) {
      throw new IllegalArgumentException(TweetVerifier.INVALID_MESSAGE);
    }
    // TODO: Escape data from the client to avoid cross-site script vulnerabilities.
    list.add(tweet);
    // 20 last tweets without history
//    list.insertFirst(tweet);
//    while (list.size() > MAX_NUMBER_OF_TWEETS) {
//      list.removeLast();
//    }
  }

  /**
   * Escape an HTML string. Escaping data received from the client helps to prevent cross-site
   * script vulnerabilities.
   *
   * @param html the html string to escape
   * @return the escaped string
   */
  protected final String escapeHtml(String html) {
    if (html == null) {
      return "";
    }
    return SimpleHtmlSanitizer.sanitizeHtml(html).asString();
  }

  /**
   * Create mock {@code Tweet} data.
   */
  private void generateTweets() {
    final List<Tweet> list = new ArrayList<Tweet>();
    final String username = "lchau";
    for (int i = 0; i < MAX_NUMBER_OF_TWEETS; i++) {
      Date date = new Date(System.currentTimeMillis() - (2L * random.nextInt(Integer.MAX_VALUE)));
      String message = createRandomMessage();
      list.add(new Tweet(date, username, message));
    }
    Collections.sort(list, Tweet.TweetComparator.DATE_DESC);
    list.addAll(list);
    System.out.println(list.size());
  }
}
