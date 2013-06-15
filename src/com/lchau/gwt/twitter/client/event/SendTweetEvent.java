package com.lchau.gwt.twitter.client.event;

import com.google.gwt.event.shared.GwtEvent;

public class SendTweetEvent extends GwtEvent<SendTweetEventHandler> {
  public static final Type<SendTweetEventHandler> TYPE = new Type<SendTweetEventHandler>();

  @Override
  public Type<SendTweetEventHandler> getAssociatedType() {
    return TYPE;
  }

  @Override
  protected void dispatch(SendTweetEventHandler handler) {
    handler.onSendTweet(this);
  }
}
