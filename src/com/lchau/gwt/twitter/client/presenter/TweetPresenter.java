package com.lchau.gwt.twitter.client.presenter;

import java.util.Date;
import java.util.List;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasAllKeyHandlers;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasEnabled;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;
import com.lchau.gwt.twitter.client.TwitterServiceAsync;
import com.lchau.gwt.twitter.client.event.ClearTweetEvent;
import com.lchau.gwt.twitter.client.event.SendTweetEvent;
import com.lchau.gwt.twitter.client.ui.TweetWidget;
import com.lchau.gwt.twitter.shared.TweetVerifier;
import com.lchau.gwt.twitter.shared.dto.Tweet;

public class TweetPresenter implements Presenter {
  public interface Display {
    Widget asWidget();

    HasClickHandlers generateRandomTweets();

    HasText getCharacterCount();

    HasText getErrorMessage();

    HasValue<String> getMessage();

    HasClickHandlers getResetButton();

    HasClickHandlers getSendTweetButton();

    HasWidgets getTwitterFeedContainer();
  }

  private class Callback<T> implements AsyncCallback<T> {
    @Override
    public void onFailure(Throwable caught) {
      showErrorMessage(SERVER_ERROR);
    }

    @Override
    public void onSuccess(T result) {
      clearErrorMessage();
    }
  }

  /**
   * The message displayed to the user when the server cannot be reached or returns an error.
   */
  private static final String SERVER_ERROR = "An error occurred while "
      + "attempting to contact the server. Please check your network "
      + "connection and try again.";

  private final Display display;
  private final HandlerManager eventBus;
  private final TwitterServiceAsync rpcService;

  public TweetPresenter(TwitterServiceAsync rpcService, HandlerManager eventBus, Display view) {
    this.rpcService = rpcService;
    this.eventBus = eventBus;
    this.display = view;
  }

  @Override
  public void init(HasWidgets container) {
    display.generateRandomTweets().addClickHandler(new ClickHandler() {
      @Override
      public void onClick(ClickEvent event) {
        rpcService.createRandomMessage(new Callback<String>() {
          @Override
          public void onSuccess(String result) {
            super.onSuccess(result);
            doSendTweet(result);
          };
        });
      }
    });
    display.getSendTweetButton().addClickHandler(new ClickHandler() {
      @Override
      public void onClick(ClickEvent event) {
        doSendTweet(display.getMessage().getValue());
      }
    });
    display.getResetButton().addClickHandler(new ClickHandler() {
      @Override
      public void onClick(ClickEvent event) {
        doClearTweets();
      }
    });

    toggleTweetButton(false);
    if (display.getMessage() instanceof HasAllKeyHandlers) {
      HasAllKeyHandlers widget = ((HasAllKeyHandlers) display.getMessage());
      widget.addKeyDownHandler(new KeyDownHandler() {
        @Override
        public void onKeyDown(KeyDownEvent event) {
          // defer events until event bus is finished
          Scheduler.get().scheduleDeferred(new ScheduledCommand() {
            @Override
            public void execute() {
              String message = display.getMessage().getValue();
              int length = message.length();
              display.getCharacterCount().setText("" + length);
              if (TweetVerifier.isValidMessage(message)) {
                toggleTweetButton(true);
                display.getErrorMessage().setText("");
              } else {
                toggleTweetButton(false);
                display.getErrorMessage().setText(TweetVerifier.INVALID_MESSAGE);
              }
            }
          });
        }
      });
    }
    doUpdateTwitterFeed();
    container.clear();
    container.add(display.asWidget());
  }

  private void clearErrorMessage() {
    showErrorMessage("");
  }

  private void doClearTweets() {
    rpcService.clearTweets(new Callback<Void>() {
      @Override
      public void onSuccess(Void result) {
        super.onSuccess(result);
        display.getTwitterFeedContainer().clear();
        eventBus.fireEvent(new ClearTweetEvent());
      }
    });
  }

  private void doSendTweet(String message) {
    final Date date = new Date();
    final String username = "lchau";

    // client-side validation
    clearErrorMessage();

    if (!TweetVerifier.isValidCreationDate(date)) {
      showErrorMessage(TweetVerifier.INVALID_CREATION_DATE);
      return;
    } else if (!TweetVerifier.isValidUsername(username)) {
      // should not occur, username is fixed
      showErrorMessage(TweetVerifier.INVALID_USERNAME);
      return;
    } else if (!TweetVerifier.isValidMessage(message)) {
      showErrorMessage(TweetVerifier.INVALID_MESSAGE);
      return;
    }

    Tweet tweet = new Tweet(date, username, message);
    rpcService.sendMessage(tweet, new Callback<Void>() {
      @Override
      public void onSuccess(Void result) {
        super.onSuccess(result);
        doUpdateTwitterFeed();
        eventBus.fireEvent(new SendTweetEvent());
      };
    });
  }

  private void doUpdateTwitterFeed() {
    rpcService.getMostRecentTweets(new Callback<List<Tweet>>() {
      @Override
      public void onSuccess(List<Tweet> result) {
        super.onSuccess(result);
        for (Tweet t : result) {
          display.getTwitterFeedContainer().add(new TweetWidget(t));
        }
      }
    });
  }

  private void showErrorMessage(String message) {
    display.getErrorMessage().setText(message);
  }

  private void toggleTweetButton(boolean isEnabled) {
    if (display.getSendTweetButton() instanceof HasEnabled) {
      ((HasEnabled) display.getSendTweetButton()).setEnabled(isEnabled);
    }
  }
}
