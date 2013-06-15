package com.lchau.gwt.twitter.client.renderers;

import java.util.Date;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.junit.Test;

import com.google.gwt.junit.client.GWTTestCase;

/**
 * Specialized unit test for client-side only elements. <br />
 * <br />
 * Note: This unit test must be run as a {@link GWTTestCase} and will fail if running as an ordinary
 * JUnit {@link TestCase}.
 *
 * @author Lam Chau
 */
public class TweetDateRendererTest extends GWTTestCase {

  @Test
  public void testRenderer() {
    // Epoch timestamp: 1184859716
    // Timestamp in milliseconds: 1184859716000
    // Human time (GMT): Thu, 19 Jul 2007 15:41:56 GMT
    // Human time (your time zone): Thursday, July 19, 2007 8:41:56 AM
    Date date = new Date(1184859716000L);
    String rendered = TweetDateRenderer.getInstance().render(date);
    Assert.assertTrue("3:41 PM - 19 Jul 07".equals(rendered));
  }

  @Test
  public void testRendererNull() {
    String rendered = TweetDateRenderer.getInstance().render(null);
    Assert.assertTrue("".equals(rendered));
  }

  @Override
  public String getModuleName() {
    return "com.lchau.gwt.twitter.Application";
  }
}
