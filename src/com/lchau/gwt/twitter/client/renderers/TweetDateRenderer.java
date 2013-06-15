package com.lchau.gwt.twitter.client.renderers;

import java.util.Date;

import com.google.gwt.i18n.client.TimeZone;
import com.google.gwt.i18n.shared.DateTimeFormat;
import com.google.gwt.text.shared.AbstractRenderer;
import com.google.gwt.text.shared.Renderer;

/**
 * {@code Date} {@link Renderer} formats using the same format as {@code http://www.twitter.com}.
 *
 * @author Lam Chau
 */
public class TweetDateRenderer extends AbstractRenderer<Date> {
  private static final TimeZone UTC_TIMEZONE = TimeZone.createTimeZone(0);
  private static final DateTimeFormat DEFAULT_FORMAT = DateTimeFormat
      .getFormat("h:mm a - d MMM yy");

  private static Renderer<Date> renderer;

  /**
   * Gets the singleton renderer to avoid unnecessary creation of objects.
   *
   * @return the renderer singleton
   */
  public static Renderer<Date> getInstance() {
    if (renderer == null) {
      renderer = new TweetDateRenderer();
    }
    return renderer;
  }

  @Override
  public String render(Date date) {
    if (date != null) {
      try {
        // assume server runs in UTC time zone
        // TODO: allow for localized time
        return DEFAULT_FORMAT.format(date, UTC_TIMEZONE);
      } catch (Exception e) {
        // renderers should never throw an exception
      }
    }
    return "";
  }
}
