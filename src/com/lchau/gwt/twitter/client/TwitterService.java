package com.lchau.gwt.twitter.client;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.lchau.gwt.twitter.shared.dto.Tweet;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("twitter")
public interface TwitterService extends RemoteService {
  // TODO: add parameter for last n tweets
  List<Tweet> getMostRecentTweets();

  void sendMessage(Tweet tweet) throws IllegalArgumentException;

  String createRandomMessage();
}
