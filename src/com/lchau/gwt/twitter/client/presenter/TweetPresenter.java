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
import com.lchau.gwt.twitter.client.event.SendTweetEvent;
import com.lchau.gwt.twitter.client.ui.TweetWidget;
import com.lchau.gwt.twitter.shared.TweetVerifier;
import com.lchau.gwt.twitter.shared.dto.Tweet;

public class TweetPresenter implements Presenter {
  /**
   * The message displayed to the user when the server cannot be reached or returns an error.
   */
  private static final String SERVER_ERROR = "An error occurred while "
      + "attempting to contact the server. Please check your network "
      + "connection and try again.";

  public interface Display {
    Widget asWidget();

    HasText getCharacterCount();

    HasText getErrorMessage();

    HasValue<String> getMessage();

    HasClickHandlers getSendTweetButton();

    HasWidgets getTwitterFeedContainer();
  }

  private final Display display;
  private final HandlerManager eventBus;
  private List<Tweet> queue;
  private final TwitterServiceAsync rpcService;

  public TweetPresenter(TwitterServiceAsync rpcService, HandlerManager eventBus, Display view) {
    this.rpcService = rpcService;
    this.eventBus = eventBus;
    this.display = view;
  }

  @Override
  public void init(HasWidgets container) {
    display.getSendTweetButton().addClickHandler(new ClickHandler() {
      @Override
      public void onClick(ClickEvent event) {
        doSendTweet();
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

  private void doSendTweet() {
    final Date date = new Date();
    final String username = "lchau";
    final String message = display.getMessage().getValue();;

    // client-side validation
    final HasText errorMessage = display.getErrorMessage();
    errorMessage.setText("");

    if (!TweetVerifier.isValidCreationDate(date)) {
      errorMessage.setText(TweetVerifier.INVALID_CREATION_DATE);
      return;
    } else if (!TweetVerifier.isValidUsername(username)) {
      // should not occur, username is fixed
      errorMessage.setText(TweetVerifier.INVALID_USERNAME);
      return;
    } else if (!TweetVerifier.isValidMessage(message)) {
      errorMessage.setText(TweetVerifier.INVALID_MESSAGE);
      return;
    }

    Tweet tweet = new Tweet(date, username, message);
    rpcService.sendMessage(tweet, new AsyncCallback<Void>() {
      @Override
      public void onFailure(Throwable caught) {
        errorMessage.setText(SERVER_ERROR);
      }

      @Override
      public void onSuccess(Void result) {
        doUpdateTwitterFeed();
        eventBus.fireEvent(new SendTweetEvent());
      };
    });
  }

  private void doUpdateTwitterFeed() {
    rpcService.getMostRecentTweets(new AsyncCallback<List<Tweet>>() {
      @Override
      public void onFailure(Throwable caught) {
        if (queue != null) {
          queue.clear();
        }
        display.getTwitterFeedContainer().clear();
      }

      @Override
      public void onSuccess(List<Tweet> result) {
        queue = result;
        // TODO: find elegant way to update the DOM of stale data, Tweet DTOs are pretty lightweight
        // so redrawing is not a huge burden client-side.
        // TODO: use Map<Tweet, TweetWidget> to cache DOM objects for faster rendering
        display.getTwitterFeedContainer().clear();
        for (Tweet t : queue) {
          display.getTwitterFeedContainer().add(new TweetWidget(t));
        }
      }
    });
  }

  private void toggleTweetButton(boolean isEnabled) {
    if (display.getSendTweetButton() instanceof HasEnabled) {
      ((HasEnabled) display.getSendTweetButton()).setEnabled(isEnabled);
    }
  }
}
