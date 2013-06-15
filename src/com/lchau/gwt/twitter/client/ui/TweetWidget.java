package com.lchau.gwt.twitter.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.safehtml.shared.SimpleHtmlSanitizer;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.lchau.gwt.twitter.client.renderers.TweetDateRenderer;
import com.lchau.gwt.twitter.shared.dto.Tweet;

/**
 * Widget representing a tweet.
 *
 * @author Lam Chau
 */
public class TweetWidget extends Composite {

  private static TweetWidgetUiBinder uiBinder = GWT.create(TweetWidgetUiBinder.class);

  interface TweetWidgetUiBinder extends UiBinder<Widget, TweetWidget> {
  }

  @UiField SpanElement user;
  @UiField DivElement message;
  @UiField DivElement date;

  public TweetWidget(Tweet tweet) {
    initWidget(uiBinder.createAndBindUi(this));
    // TODO: add profile image
    user.setInnerHTML(tweet.getUser());
    // escape html in message (sanitize)
    message.setInnerSafeHtml(SimpleHtmlSanitizer.sanitizeHtml(tweet.getMessage()));
    date.setInnerHTML(TweetDateRenderer.getInstance().render(tweet.getCreationDate()));
  }
}
