package main.Linux3000.audio;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import main.Linux3000.DiscordBot;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.HashMap;
import java.util.Map;

public class MusicManagerController {

    private Map<Guild, GuildMusicManager> guildCache;
    private HashMap<Guild, TextChannel> textChannels = new HashMap<>();
    public MusicManagerController() {
        guildCache = new HashMap<>();
    }

    public void addEntry(Guild guild, GuildMusicManager manager) {
        guildCache.put(guild, manager);
        System.out.println(guildCache);
    }

    public Guild getGuildByPlayer(AudioPlayer player) {
        for(Guild guild : guildCache.keySet()) {
            if(guildCache.get(guild).getAudioPlayer().equals(player)) {
                return guild;
            }
        }
        return null;
    }

    public void removeGuildFromCache(Guild guild) {
        guildCache.remove(guild);
    }

    public void addChannelToGuild(Guild guild, TextChannel channel) {
        textChannels.put(guild, channel);
    }

    public TextChannel getSpecifiedTextChannel(Guild guild) {
        return textChannels.getOrDefault(guild, null);
    }



}
