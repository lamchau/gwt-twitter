package com.lchau.gwt.twitter.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 *
 */
public class Application implements EntryPoint {
  /**
   * Create a remote service proxy to talk to the server-side Greeting service.
   */
  private final TwitterServiceAsync rpcService = GWT.create(TwitterService.class);

  /**
   * This is the entry point method.
   */
  @Override
  public void onModuleLoad() {
    HandlerManager eventBus = new HandlerManager(null);
    Controller controller = new Controller(rpcService, eventBus);
    controller.init(RootPanel.get());
  }
}
