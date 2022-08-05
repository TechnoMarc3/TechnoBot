package main.Linux3000.youtube;

public class YoutubeVideo {

    private String title;
    private String dateTime;
    private String channelId;
    private String videoId;

    public YoutubeVideo(String title, String dateTime, String videoId, String channelTitle) {
        this.title = title;
        this.dateTime = dateTime;
        this.channelId = channelTitle;
        this.videoId = videoId;
    }

    public String getTitle() {
        return title;
    }

    public String getDateTime() {
        return dateTime;
    }

    public String getChannelTitle() {
        return channelId;
    }

    public String getVideoId() {
        return videoId;
    }
}
