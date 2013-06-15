package com.lchau.gwt.twitter.client;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.HasWidgets;
import com.lchau.gwt.twitter.client.event.ClearTweetEvent;
import com.lchau.gwt.twitter.client.event.ClearTweetEventHandler;
import com.lchau.gwt.twitter.client.event.SendTweetEvent;
import com.lchau.gwt.twitter.client.event.SendTweetEventHandler;
import com.lchau.gwt.twitter.client.presenter.Presenter;
import com.lchau.gwt.twitter.client.presenter.TweetPresenter;
import com.lchau.gwt.twitter.client.view.ApplicationView;

public class Controller implements Presenter, ValueChangeHandler<String> {

  private HasWidgets container;
  private final HandlerManager eventBus;
  private final TwitterServiceAsync rpcService;

  public Controller(TwitterServiceAsync rpcService, HandlerManager eventBus) {
    this.rpcService = rpcService;
    this.eventBus = eventBus;
    initComponents();
  }

  private void initComponents() {
    History.addValueChangeHandler(this);
    eventBus.addHandler(SendTweetEvent.TYPE, new SendTweetEventHandler() {
      @Override
      public void onSendTweet(SendTweetEvent event) {
        History.fireCurrentHistoryState();
      }
    });
    eventBus.addHandler(ClearTweetEvent.TYPE, new ClearTweetEventHandler() {
      @Override
      public void onClearTweet(ClearTweetEvent event) {
        // cleared
      }
    });
  }

  @Override
  public void init(HasWidgets container) {
    this.container = container;
    History.fireCurrentHistoryState();
  }

  @Override
  public void onValueChange(ValueChangeEvent<String> event) {
    Presenter presenter = new TweetPresenter(rpcService, eventBus, new ApplicationView());
    presenter.init(container);
  }
}
