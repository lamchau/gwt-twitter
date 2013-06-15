package com.lchau.gwt.twitter.client.view;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.lchau.gwt.twitter.client.presenter.TweetPresenter;

public class ApplicationView extends Composite implements TweetPresenter.Display {

  interface ApplicationViewUiBinder extends UiBinder<Widget, ApplicationView> {
  }

  private static ApplicationViewUiBinder uiBinder = GWT.create(ApplicationViewUiBinder.class);

  @UiField
  InlineLabel characterCount;
  @UiField(provided = true)
  Label errorMessage;
  @UiField
  HasValue<String> message;
  @UiField
  HasClickHandlers generateTweetsButton;
  @UiField
  HasClickHandlers resetButton;
  @UiField
  HasClickHandlers sendTweet;
  @UiField
  FlowPanel twitterFeed;

  public ApplicationView() {
    // auto-hiding error message
    errorMessage = new Label() {
      @Override
      public void setText(String text) {
        setVisible(text != null && text.length() > 0);
        super.setText(text);
      }
    };
    initWidget(uiBinder.createAndBindUi(this));
  }

  @Override
  public HasText getCharacterCount() {
    return characterCount;
  }

  @Override
  public HasText getErrorMessage() {
    return errorMessage;
  }

  @Override
  public HasValue<String> getMessage() {
    return message;
  }

  @Override
  public HasClickHandlers getResetButton() {
    return resetButton;
  }

  @Override
  public HasClickHandlers getSendTweetButton() {
    return sendTweet;
  }

  @Override
  public HasWidgets getTwitterFeedContainer() {
    return twitterFeed;
  }

  @Override
  public HasClickHandlers generateRandomTweets() {
    return generateTweetsButton;
  }
}
