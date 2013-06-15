package com.lchau.gwt.twitter.client.event;

import com.google.gwt.event.shared.GwtEvent;

public class ClearTweetEvent extends GwtEvent<ClearTweetEventHandler> {
  public static final Type<ClearTweetEventHandler> TYPE = new Type<ClearTweetEventHandler>();

  @Override
  public Type<ClearTweetEventHandler> getAssociatedType() {
    return TYPE;
  }

  @Override
  protected void dispatch(ClearTweetEventHandler handler) {
    handler.onClearTweet(this);
  }
}
