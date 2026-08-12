package ${subscribePackage};

public enum Channels {

<#list channels as channel>
  ${channel.constantName}("${channel.channelValue}")<#if channel?has_next>,</#if><#if !channel?has_next>;</#if>
</#list>

  private final String channel;

  Channels(final String channel) {
    this.channel = channel;
  }

  public String getChannel() {
    return channel;
  }
}