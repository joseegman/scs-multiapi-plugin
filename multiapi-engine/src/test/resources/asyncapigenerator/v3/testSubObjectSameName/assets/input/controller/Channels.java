package input.controller;

public enum Channels {

  OUTPUT("events/public/output/(.*)"),
  INPUT("events/public/input/(.*)");

  private final String channel;

  Channels(final String channel) {
    this.channel = channel;
  }

  public String getChannel() {
    return channel;
  }
}