package com.lchau.gwt.twitter.client;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.lchau.gwt.twitter.shared.dto.Tweet;

/**
 * The async counterpart of <code>TwitterService</code>.
 */
public interface TwitterServiceAsync {
  void sendMessage(Tweet tweet, AsyncCallback<Void> callback)
      throws IllegalArgumentException;

  void getMostRecentTweets(AsyncCallback<List<Tweet>> callback);

  void createRandomMessage(AsyncCallback<String> callback);
}
