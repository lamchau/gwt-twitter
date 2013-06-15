package com.lchau.gwt.twitter.client.event;

import com.google.gwt.event.shared.EventHandler;

public interface SendTweetEventHandler extends EventHandler {
  void onSendTweet(SendTweetEvent event);
}
