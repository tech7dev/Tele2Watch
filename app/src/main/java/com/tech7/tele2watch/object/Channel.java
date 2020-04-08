package com.tech7.tele2watch.object;

public class Channel {
    private String Tvg_logo;
    private String Group_title;
    private String ChannelUrl;

    public Channel(){

    }

    public Channel(String Tvg_logo, String Group_title, String ChannelUrl){
        this.Tvg_logo = Tvg_logo;
        this.Group_title = Group_title;
        this.ChannelUrl = ChannelUrl;
    }

    public String getTvg_logo() {
        return Tvg_logo;
    }

    public String getGroup_title() {
        return Group_title;
    }

    public String getChannelUrl() {
        return ChannelUrl;
    }

    public void setTvg_logo(String tvg_logo) {
        Tvg_logo = tvg_logo;
    }

    public void setGroup_title(String group_title) {
        Group_title = group_title;
    }

    public void setChannelUrl(String channelUrl) {
        ChannelUrl = channelUrl;
    }
}
