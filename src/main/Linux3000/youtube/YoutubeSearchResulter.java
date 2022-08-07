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




    public String getChannelId() {
        return channelId;
    }

    public Guild getGuild() {
        return guild;
    }
}
