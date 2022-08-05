package main.Linux3000.youtube;

import main.Linux3000.DiscordBot;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import javax.annotation.Nullable;
import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class YoutubeSearchResulter {



    private HashMap<Guild, List<YoutubeVideo>> allVideos = new HashMap<>();
    private HashMap<Guild, List<YoutubeVideo>> lastVideos = new HashMap<>();
    private YoutubeVideo newest = null;

    private String channelId;
    private Guild guild;
    public YoutubeSearchResulter() {
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public void setGuild(Guild guild) {
        this.guild = guild;
    }

    String url ;

    public JSONObject readJsonFromUrl()throws IOException, JSONException {
        url = "https://www.googleapis.com/youtube/v3/search?order=date&part=snippet&channelId=" + getChannelId() + "&maxResults=20&key=AIzaSyAC32Iu7AsqocF9Wna-M3oSqH0N5c2aDvM";
        InputStream is = new URL(url).openStream();
        try {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
            String jsonText = readAll(rd);
            JSONObject json = new JSONObject(jsonText);
            return json;
        } finally {
            is.close();
        }
    }
    private static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

    public void runFirst() throws IOException {
        List<YoutubeVideo> videos = new ArrayList<>();
        JSONObject object = readJsonFromUrl();
        JSONArray array = object.getJSONArray("items");
        for(int i = 0; i<array.length(); i++) {
            JSONObject obj = array.getJSONObject(i);
            JSONObject sv = obj.getJSONObject("id");
            String s =  sv.getString("kind");
            if(s.equalsIgnoreCase("youtube#video")) {
                JSONObject snippet = obj.getJSONObject("snippet");
                String publish = snippet.getString("publishedAt");
                String title = snippet.getString("title");
                String channelId = snippet.getString("channelTitle");
                String videoId = "https://www.youtube.com/watch?v=" + sv.getString("videoId");
                YoutubeVideo video = new YoutubeVideo(title, publish, videoId, channelId);
                videos.add(video);
            }
        }
        allVideos.put(this.guild, videos);
    }
    public void runCircle() throws IOException {
        List<YoutubeVideo> videos = new ArrayList<>();
        JSONObject object = readJsonFromUrl();
        JSONArray array = object.getJSONArray("items");
        for(int i = 0; i<array.length(); i++) {
            JSONObject obj = array.getJSONObject(i);
            JSONObject sv = obj.getJSONObject("id");
            String s =  sv.getString("kind");
            if(s.equalsIgnoreCase("youtube#video")) {
                JSONObject snippet = obj.getJSONObject("snippet");
                String publish = snippet.getString("publishedAt");
                String title = snippet.getString("title");
                String channelId = snippet.getString("channelTitle");
                String videoId = "https://www.youtube.com/watch?v=" + sv.getString("videoId");
                YoutubeVideo video = new YoutubeVideo(title, publish, videoId, channelId);
                videos.add(video);
            }
        }
        lastVideos.put(this.guild, videos);
    }


    public void check() {

        if(!allVideos.get(this.guild).get(0).getTitle().equalsIgnoreCase(lastVideos.get(this.guild).get(0).getTitle())) {
            System.out.println("ja");
            newest = lastVideos.get(this.guild).get(0);

            TextChannel channel = DiscordBot.INSTANCE.jda.getTextChannelById("868798821092696087");
            channel.sendMessage("UPLOAD: " + newest.getChannelTitle() + " Video: " + newest.getTitle() + " URL: " + newest.getVideoId()).queue();

        }
        allVideos.remove(this.guild);
        allVideos.put(this.guild, lastVideos.get(this.guild));
        lastVideos.remove(this.guild);
    }

    public String getChannelId() {
        return channelId;
    }

    public Guild getGuild() {
        return guild;
    }
}
